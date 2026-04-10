@file:Suppress("Unused", "NOTHING_TO_INLINE")

package xyz.aerii.library.internal.events.core

import xyz.aerii.library.handlers.Observable

internal inline fun <reified T : Event> on(
    priority: Int = 0,
    noinline handler: T.() -> Unit
) = Node(T::class.java, handler, priority).apply { register() }

internal fun Node<*>.runWhen(state: Observable<Boolean>) = apply {
    if (overridden) return@apply
    add(state)
}

internal fun Node<*>.override(state: Observable<Boolean>) = apply {
    overridden = true
    conditions.clear()
    add(state)
}

internal fun Node<*>.override() = apply {
    overridden = true
    conditions.clear()
    register()
}

private fun Node<*>.add(state: Observable<Boolean>) = apply {
    conditions.add(state)
    state.onChange { evaluate() }
    evaluate()
}