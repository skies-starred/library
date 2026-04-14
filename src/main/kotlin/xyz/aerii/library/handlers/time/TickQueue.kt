package xyz.aerii.library.handlers.time

import xyz.aerii.library.utils.safely
import java.util.concurrent.ConcurrentHashMap

class TickQueue {
    private val pending = ConcurrentHashMap<Long, ArrayDeque<TickTask>>()
    private var current = 0L

    fun tick() {
        pending.remove(++current)?.forEach { task ->
            if (task.cancelled) return@forEach
            safely { task.action() }
            task.interval?.let { pending.getOrPut(current + it) { ArrayDeque() } += task }
        }
    }

    fun schedule(delay: Long, action: () -> Unit): Task {
        val task = TickTask(action)
        pending.getOrPut(current + delay) { ArrayDeque() } += task
        return task
    }

    fun repeat(interval: Long, delay: Long, action: () -> Unit): Task {
        val task = TickTask(action, interval)
        pending.getOrPut(current + delay) { ArrayDeque() } += task
        return task
    }
}