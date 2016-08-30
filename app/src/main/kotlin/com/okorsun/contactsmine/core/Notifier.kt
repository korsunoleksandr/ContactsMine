package com.okorsun.contactsmine.core

import rx.Observable
import rx.subjects.BehaviorSubject
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Created by okorsun on 29.08.16.
 */
class Notifier
@Inject constructor() {

    companion object {
        val DEFAULT = Notifier()
    }

    private val notifiers: ConcurrentHashMap<KClass<*>, BehaviorSubject<Int>> = ConcurrentHashMap()

    val KClass<*>.changes: Observable<Int>
        get() = getOrCreateNotifier(this)

    fun KClass<*>.notifyChange() {
        getOrCreateNotifier(this).apply {
            onNext(value + 1)
        }
    }

    fun <T> Observable<T>.notifyOnNext(cls: KClass<*>): Observable<T> = doOnNext {
        cls.notifyChange()
    }

    private fun getOrCreateNotifier(cls: KClass<*>) = notifiers.getOrPut(cls) { BehaviorSubject.create(0) }

}

fun <T> withRefetch(notifier: Notifier = Notifier.DEFAULT,
                    lock: KClass<*>,
                    f: () -> Observable<T>): Observable<T> = with(notifier) {
    lock.changes.switchMap { f() }
}

inline fun <T> withNotify(notifier: Notifier = Notifier.DEFAULT,
                          lock: KClass<*>,
                          f: () -> Observable<T>): Observable<T> = with(notifier) {
    f().notifyOnNext(lock)
}

inline fun notify(notifier: Notifier = Notifier.DEFAULT,
                  lock: KClass<*>,
                  f: () -> Unit) = with(notifier) {
    f()
    lock.notifyChange()
}