package presentation.base

import android.view.View
import com.pedrogomez.renderers.Renderer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by okorsun on 16.03.16.
 */
abstract class KRenderer<T> : Renderer<T>() {
    private var bindings = mutableMapOf<Int, View>()

    protected fun <V : View> bindView(id: Int) = PropertyBinder<V>(id)

    override fun clone(): Any =
            (super.clone() as KRenderer<*>).apply {
                this@apply.bindings = mutableMapOf<Int, View>()
            }


    @Suppress("UNCHECKED_CAST")
    class PropertyBinder<V>(val viewId: Int) : ReadOnlyProperty<KRenderer<*>, V>, Cloneable {

        override fun getValue(thisRef: KRenderer<*>, property: KProperty<*>): V =
                thisRef.bindings.getOrPut(viewId) {
                    thisRef.rootView.findViewById(viewId) ?:
                            throw IllegalStateException("View ID $viewId for '${property.name}' not found.")
                } as V

    }
}