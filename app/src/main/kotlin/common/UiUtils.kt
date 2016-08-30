package common

import com.pedrogomez.renderers.ListAdapteeCollection
import com.pedrogomez.renderers.RVRendererAdapter
import com.pedrogomez.renderers.Renderer
import com.pedrogomez.renderers.RendererBuilder

/**
 * Created by okorsun on 30.08.16.
 */


fun <T> createRendererAdapter(renderer: Renderer<T>): RVRendererAdapter<T> {
    val rendererBuilder = RendererBuilder(renderer)
    return RVRendererAdapter(rendererBuilder, ListAdapteeCollection<T>())
}