@file:Suppress("Unused")

package xyz.aerii.library.handlers

import kotlinx.atomicfu.atomic
import java.util.concurrent.CopyOnWriteArrayList

class Observable<T>(initial: T) {
    private val state = atomic(initial)
    private val immutable = atomic(false)
    private val listeners = CopyOnWriteArrayList<(T) -> Unit>()

    var value: T
        get() = state.value
        set(new) {
            if (immutable.value) throw UnsupportedOperationException("Reactive value is set as immutable.")

            val old = state.getAndSet(new)
            if (old != new) {
                val snapshot = listeners.toTypedArray()
                for (i in snapshot) i(new)
            }
        }

    fun onChange(callback: (T) -> Unit) = apply {
        listeners.add(callback)
    }

    fun immutable() = apply {
        immutable.value = true
    }

    fun <R> map(transform: (T) -> R): Observable<R> {
        val mapped = Observable(transform(value))
        onChange { mapped.value = transform(it) }
        return mapped
    }

    fun <O, R> combine(other: Observable<O>, transform: (T, O) -> R): Observable<R> {
        val combined = Observable(transform(value, other.value))
        val update = { combined.value = transform(this.value, other.value) }

        onChange { update() }
        other.onChange { update() }

        return combined
    }

    fun filter(predicate: (T) -> Boolean): Observable<T> {
        val filtered = Observable(value)
        onChange { if (predicate(it)) filtered.value = it }
        return filtered
    }

    fun scan(initial: T, operation: (acc: T, value: T) -> T): Observable<T> {
        val scanned = Observable(initial)
        onChange { scanned.value = operation(scanned.value, it) }
        return scanned
    }

    companion object {
        infix fun Observable<Boolean>.and(other: Observable<Boolean>): Observable<Boolean> {
            return combine(other) { a, b -> a && b }
        }

        infix fun Observable<Boolean>.or(other: Observable<Boolean>): Observable<Boolean> {
            return combine(other) { a, b -> a || b }
        }
    }
}