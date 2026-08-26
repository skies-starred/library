@file:Suppress("Unused", "Unchecked_Cast")

package foo.starred.snowbird.api.lazy

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CallbackLazy<T>(
    private val fn0: () -> Unit,
    private val fn1: () -> T
) : ReadOnlyProperty<Any?, T> {
    private var init = false
    private var v: T? = null

    fun init() {
        v = fn1()
        init = true
        fn0()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (!init) init()
        return v as T
    }
}
