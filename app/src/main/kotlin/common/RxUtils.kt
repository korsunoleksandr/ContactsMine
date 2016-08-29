package common

import rx.Observable

/**
 * Created by okorsun on 29.08.16.
 */


fun <T> createObservable(action: () -> T): Observable<T> =
        Observable.create({ subscriber ->
            try {
                val data = action.invoke()
                subscriber.onNext(data)
                subscriber.onCompleted()
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        })