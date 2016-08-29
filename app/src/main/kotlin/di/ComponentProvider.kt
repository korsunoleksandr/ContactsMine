package di

import java.util.*

class ComponentProvider {

    private val componentMap = HashMap<String, Any>()

    fun putComponent(name: String, component: Any) = componentMap.put(name, component)

    @Suppress("UNCHECKED_CAST")
    fun <T> getComponent(name: String): T = componentMap[name] as T

    fun hasComponent(name: String): Boolean = componentMap.containsKey(name)

    fun removeComponent(name: String) = componentMap.remove(name)
}