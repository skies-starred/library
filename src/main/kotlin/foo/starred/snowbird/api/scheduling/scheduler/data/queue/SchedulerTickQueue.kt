package foo.starred.snowbird.api.scheduling.scheduler.data.queue

import foo.starred.snowbird.api.scheduling.scheduler.data.tasks.base.SchedulerTask
import foo.starred.snowbird.api.scheduling.scheduler.data.tasks.impl.SchedulerTickTask
import foo.starred.snowbird.utils.safely
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class SchedulerTickQueue {
    private val pending = ConcurrentHashMap<Long, ConcurrentLinkedQueue<SchedulerTickTask>>()
    private var current = 0L

    fun tick() {
        pending.remove(++current)?.forEach { task ->
            if (task.cancelled) return@forEach
            safely { task.action() }
            task.interval?.let { pending.computeIfAbsent(current + it) { ConcurrentLinkedQueue() } += task }
        }
    }

    fun schedule(delay: Long, action: () -> Unit): SchedulerTask {
        val task = SchedulerTickTask(action)
        pending.computeIfAbsent(current + delay) { ConcurrentLinkedQueue() } += task
        return task
    }

    fun repeat(interval: Long, delay: Long, action: () -> Unit): SchedulerTask {
        val task = SchedulerTickTask(action, interval)
        pending.computeIfAbsent(current + delay) { ConcurrentLinkedQueue() } += task
        return task
    }
}
