package presentation.base

import rx.Notification

data class Delivery<out V, T>(val view: V, val notification: Notification<T>) {

    fun split(onNext: (V, T) -> Unit, onError: (V, Throwable) -> Unit, onCompleted: (V) -> Unit) =
            when (notification.kind) {
                Notification.Kind.OnNext -> onNext(view, notification.value)
                Notification.Kind.OnError -> onError(view, notification.throwable)
                Notification.Kind.OnCompleted -> onCompleted(view)
                else -> throw IllegalArgumentException("Notification.Kind = ${notification.kind}")
            }

}