package com.okorsun.contactsmine.presentation.base

import android.os.Bundle
import com.okorsun.contactsmine.presentation.base.Presenter.LifecycleEvent.*
import rx.Notification
import rx.Observable
import rx.Subscription
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import rx.subjects.ReplaySubject
import timber.log.Timber
import kotlin.reflect.KClass

abstract class Presenter<V : PresenterView>() {
    private val permissionManager: PermissionManager = PermissionManager()

    enum class LifecycleEvent {
        CREATE,
        ATTACH,
        DETACH,
        DESTROY
    }

    private val lifecycle = PublishSubject.create<LifecycleEvent>()

    protected val views = BehaviorSubject.create<V?>()
    protected val presenterViews = BehaviorSubject.create<PresenterView?>()

    private var delegate = ViewDelegate<V>()
    protected val view: V by delegate

    protected val postponedRouterCalls = mutableListOf<() -> Unit>()

    protected var createdView: V? = null

    protected val routers = mutableMapOf<KClass<out Router<in V>>, Router<in V>>()

    init {
        lifecycle.onNext(CREATE)
    }

    internal fun viewCreated(view: V) {
        routers.values.forEach {
            attachRouter(it, view)
        }
        createdView = view

        postponedRouterCalls.toList().forEach {
            it()
        }
        postponedRouterCalls.clear()
    }

    fun attachRouter(it: Router<in V>, view: V) {
        view.attachRouter(it)
        it.attachView(view)
    }

    internal fun viewDestroyed(view: V) {
        routers.values.forEach {
            view.detachRouter(it)
            it.detach()
        }
        createdView = null

    }

    inline fun<reified R : Router<in V>> route(crossinline action: R.() -> Unit) {
        val router = routers.getOrPut(R::class) {
            R::class.java.newInstance().apply {
                val cv = createdView
                if (cv != null) {
                    attachRouter(this, cv)
                }
            }
        } as R

        if (createdView != null) {
            router.action()
        } else {
            Timber.w("user of rout when activity is destroyed")
            postponedRouterCalls.add({ router.action() })
        }
    }

    internal fun attach(view: V) {
        delegate.attach(view)
        lifecycle.onNext(ATTACH)
        views.onNext(view)
        presenterViews.onNext(view)
        onAttach()
    }

    internal fun detach() {
        lifecycle.onNext(DETACH)
        presenterViews.onNext(null)
        views.onNext(null)
        onDetach()
        delegate.detach()
    }

    internal fun destroy() {
        lifecycle.onNext(DESTROY)
        onDestroy()
        views.onCompleted()
    }

    internal fun saveState(outState: Bundle) {
        onSaveInstanceState(outState)
    }

    internal fun restoreState(savedInstanceState: Bundle) {
        onRestoreInstanceState(savedInstanceState)
        Observable.create<Int> { }
    }

    protected open fun onAttach() {
        // no-op
    }

    protected open fun onDetach() {
        // no-op
    }

    protected fun onDestroy() {
        // no-op
    }

    protected fun onSaveInstanceState(outState: Bundle) {
        // no-op
    }

    protected fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // no-op
    }

    protected fun <T> Observable<T>.bindUntilEvent(event: LifecycleEvent): Observable<T> =
            compose(this@Presenter.bindUntilEvent(event))

    protected fun <T> bindUntilEvent(event: LifecycleEvent): Observable.Transformer<in T, T> =
            Observable.Transformer {
                source -> source.takeUntil(
                        lifecycle.takeFirst {
                            it == event
                        }
                )
            }

    fun <T> Observable<T>.latestCache(): Observable<Delivery<V, T>> {
        return compose<Delivery<V, T>>(applyLatestCache())
    }

    protected fun <T> Observable<T>.oneTime(): Observable<Delivery<V, T>> {
        return compose<Delivery<V, T>>(applyLatestCache()).take(1)
    }

    private fun <T> applyLatestCache(): Observable.Transformer<T, Delivery<V, T>> =
            Observable.Transformer { source ->
                val subject = ReplaySubject.createWithSize<Notification<T>>(2)
                val subscription = source.materialize().subscribe(subject)

                views.switchMap {
                    v -> subject.filter {
                        item ->

                        if (!item.isOnNext) {
                            return@filter true
                        }

                        val items = subject.getValues(arrayOfNulls<Notification<T>>(subject.size()))
                        val lastOnNextIndex = if (items.last().isOnCompleted) {
                            items.count() - 2
                        } else {
                            items.count() - 1
                        }

                        val itemIndex = items.lastIndexOf(item)
                        val isLast = itemIndex == lastOnNextIndex
                        return@filter isLast

                    }.map {
                    Delivery<V?, T>(v, it)
                    }
                }.doOnUnsubscribe {
                    subscription.unsubscribe()
                }.compose(bindUntilEvent<Delivery<V?, T>>(DESTROY))
                        .filter { it.view != null }
                        .map { it as Delivery<V, T> }
            }

    internal open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    protected fun <T> Observable<T>.requirePermissions(vararg permissions: String): Observable<T>
            = requirePermissions(permissions.toSet())

    protected fun <T> Observable<T>.requirePermissions(permissions: Set<String>): Observable<T>
            = compose<T>(requirePermissionsTransformer(permissions))

    private fun <T> requirePermissionsTransformer(permissions: Set<String>): Observable.Transformer<T, T> = Observable.Transformer {
        source -> permissionManager.requestPermissions(permissions, presenterViews).switchMap { source }
    }

    fun <T> Observable<Delivery<V, T>>.subscribe(onNext: (V, T) -> Unit = { v, data -> },
                                                 onError: (V, Throwable) -> Unit = { v, t -> },
                                                 onCompleted: (V) -> Unit = { }): Subscription {
        return subscribe { delivery -> delivery.split(onNext, onError, onCompleted) }
    }

}
