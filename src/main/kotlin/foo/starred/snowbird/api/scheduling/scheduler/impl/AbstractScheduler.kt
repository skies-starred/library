@file:Suppress("Unused")

package foo.starred.snowbird.api.scheduling.scheduler.impl

import foo.starred.snowbird.api.scheduling.scheduler.data.queue.SchedulerTickQueue
import foo.starred.snowbird.api.scheduling.scheduler.data.tasks.base.SchedulerTask
import foo.starred.snowbird.api.scheduling.scheduler.data.tasks.impl.SchedulerTimerTask
import foo.starred.snowbird.api.scheduling.scheduler.data.ticks.SchedulerTicks
import foo.starred.snowbird.utils.safely
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

open class AbstractScheduler {
    private val client0 = SchedulerTickQueue()
    private val client1 = SchedulerTickQueue()
    private val server = SchedulerTickQueue()
    private val executor = Executors.newSingleThreadScheduledExecutor { Thread(it, "Snowbird-scheduler").apply { isDaemon = true } }

    private val ClientTicks.queue: SchedulerTickQueue
        get() = if ((packed and 1L) == 1L) client0 else client1

    val ticks = SchedulerTicks()

    fun client0() {
        client0.tick()
    }

    fun client1() {
        ticks.client++
        client1.tick()
    }

    fun server() {
        ticks.server++
        server.tick()
    }

    fun schedule(delay: ClientTicks, action: () -> Unit): SchedulerTask {
        return delay.queue.schedule(delay.value.toLong(), action)
    }

    fun schedule(delay: ServerTicks, action: () -> Unit): SchedulerTask {
        return server.schedule(delay.value.toLong(), action)
    }

    fun schedule(delay: Duration, action: () -> Unit): SchedulerTask {
        return SchedulerTimerTask(executor.schedule({ safely { action() } }, delay.inWholeMilliseconds, TimeUnit.MILLISECONDS))
    }

    fun repeat(interval: ClientTicks, delay: ClientTicks = interval, action: () -> Unit): SchedulerTask {
        require((interval.packed and 1L) == (delay.packed and 1L)) { "Delay phase must match interval phase" }
        return interval.queue.repeat(interval.value.toLong(), delay.value.toLong(), action)
    }

    fun repeat(interval: ServerTicks, delay: ServerTicks = interval, action: () -> Unit): SchedulerTask {
        return server.repeat(interval.value.toLong(), delay.value.toLong(), action)
    }

    fun repeat(interval: Duration, delay: Duration = interval, action: () -> Unit): SchedulerTask {
        return SchedulerTimerTask(executor.scheduleAtFixedRate(action, delay.inWholeMilliseconds, interval.inWholeMilliseconds, TimeUnit.MILLISECONDS))
    }

    @JvmInline
    value class ClientTicks internal constructor(internal val packed: Long) {
        internal val value: Int
            get() = (packed shr 1).toInt()

        constructor(value: Int, start: Boolean = false) : this((value.toLong() shl 1) or (if (start) 1 else 0))
    }

    @JvmInline
    value class ServerTicks(val value: Int)
}
