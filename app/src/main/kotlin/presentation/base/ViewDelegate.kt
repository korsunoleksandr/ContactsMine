package presentation.base

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ViewDelegate<V> : ReadWriteProperty<Any?, V> {

    private var value: V? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): V =
            value ?: throw IllegalStateException("view should not be accessed before onAttach and after onDetach")

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        this.value = value
    }

    fun attach(value: V) {
        this.value = value
    }

    fun detach() {
        this.value = null
    }

}