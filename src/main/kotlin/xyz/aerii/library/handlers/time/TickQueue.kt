package xyz.aerii.library.handlers.time

import xyz.aerii.library.utils.safely
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class TickQueue {
    private val pending = ConcurrentHashMap<Long, ConcurrentLinkedQueue<TickTask>>()
    private var current = 0L

    fun tick() {
        pending.remove(++current)?.forEach { task ->
            if (task.cancelled) return@forEach
            safely { task.action() }
            task.interval?.let { pending.computeIfAbsent(current + it) { ConcurrentLinkedQueue() } += task }
        }
    }

    fun schedule(delay: Long, action: () -> Unit): Task {
        val task = TickTask(action)
        pending.computeIfAbsent(current + delay) { ConcurrentLinkedQueue() } += task
        return task
    }

    fun repeat(interval: Long, delay: Long, action: () -> Unit): Task {
        val task = TickTask(action, interval)
        pending.computeIfAbsent(current + delay) { ConcurrentLinkedQueue() } += task
        return task
    }
}