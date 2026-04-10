@file:Suppress("Unused")

package xyz.aerii.library.handlers.delegate

open class AbstractTickable<T>(
    private val ticks: Int = 1,
    private val getter: () -> Int,
    private val block: () -> T
) {
    private var _v: T? = null

    var init = false
        private set

    var last = -1
        private set

    val value: T?
        get() {
            val now = getter()
            if (init && now - last < ticks) return _v

            _v = block()
            last = now
            init = true

            return _v
        }

    fun reset() {
        if (!init) return

        init = false
        _v = null
        last = -1
    }
}