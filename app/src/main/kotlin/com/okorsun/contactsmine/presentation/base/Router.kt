package com.okorsun.contactsmine.presentation.base

import android.app.Activity
import android.content.Intent
import com.okorsun.contactsmine.app.BaseException
import rx.Observable
import rx.subjects.PublishSubject
import timber.log.Timber

open class Router<V : RouterView> {
    private val activityResults = PublishSubject.create<ActivityResult>()

    private var delegate = ViewDelegate<V>()
    protected val view: V by delegate

    protected inline fun <reified T : Activity> startActivity(vararg params: Pair<String, Any>) {
        val intent = AnkoUtils.createIntent(view.appContext, T::class.java, params)
        view.startActivity(intent)
    }

    protected inline fun <reified T : Activity> startActivityWithFlags(flags: Int?, vararg params: Pair<String, Any>) {
        val intent = AnkoUtils.createIntent(view.appContext, T::class.java, params)

        if (flags != null) {
            intent.flags = flags
        }

        view.startActivity(intent)
    }

    protected fun startActivityForResult(intent: Intent): Observable<ActivityResult> {
        val requestCode = nextRequestCode
        val result = activityResults
                .filter { it.requestCode == requestCode }
                .switchMap { result ->
                    if (result.resultCode == Activity.RESULT_CANCELED ) {
                        Observable.error<ActivityResult>(BadResultCodeException(result.resultCode, result.data))
                    } else {
                        Observable.just(result)
                    }
                }
                .take(1)
                .doOnSubscribe { view.startActivity(intent, requestCode) }


        return result
    }


    protected inline fun <reified T : Activity> startActivityForResult(
            vararg params: Pair<String, Any>): Observable<ActivityResult> {
        val intent = AnkoUtils.createIntent(view.appContext, T::class.java, params)
        return startActivityForResult(intent)
    }

    fun attachView(view: V) {
        delegate.attach(view)
    }

    fun detach() {
        delegate.detach()
    }

    internal fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = ActivityResult(requestCode, resultCode, data)
        Timber.d("activityResult: $result")
        activityResults.onNext(result)
    }


    companion object {
        var nextRequestCode = 100
            get() = field++
    }
}

class BadResultCodeException(val resultCode: Int, data: Intent?) : BaseException("bad result code $resultCode, data = $data")

data class ActivityResult(val requestCode: Int, val resultCode: Int, val data: Intent?)