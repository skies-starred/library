@file:Suppress("Unused", "Unchecked_Cast")

package xyz.aerii.library.handlers.delegate

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Deferred<T>(
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