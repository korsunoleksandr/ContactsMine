package presentation.base

import android.os.Bundle

internal abstract class BasePresenter<V> {

    private var view: V? = null

    internal open fun attach(view: V) {
        this.view = view
        onAttach()
    }

    internal open fun detach() {
        view = null
        onDetach()
    }

    internal open fun destroy() {
        onDestroy()
    }

    internal fun saveState(outState: Bundle) {
        onSaveInstanceState(outState)
    }

    internal fun restoreState(savedInstanceState: Bundle) {
        onRestoreInstanceState(savedInstanceState)
    }

    protected fun hasView(): Boolean {
        return view != null
    }

    protected fun getView(): V {
        if (!hasView()) {
            throw IllegalStateException("calling getView() when no view is attached")
        }

        return view!!
    }

    protected abstract fun onAttach()

    protected abstract fun onDetach()

    protected fun onDestroy() {
        // no-op
    }

    protected fun onSaveInstanceState(outState: Bundle) {
        // no-op
    }

    protected fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // no-op
    }
}