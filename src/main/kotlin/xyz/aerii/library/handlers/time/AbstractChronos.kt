@file:Suppress("Unused")

package xyz.aerii.library.handlers.time

import xyz.aerii.library.utils.safely
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

open class AbstractChronos {
    private val client0 = TickQueue()
    private val client1 = TickQueue()
    private val server = TickQueue()
    private val executor = Executors.newSingleThreadScheduledExecutor { Thread(it, "solstice-time").apply { isDaemon = true } }

    private val ClientTicks.queue: TickQueue
        get() = if ((packed and 1L) == 1L) client0 else client1

    val ticks = Ticks()

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

    fun schedule(delay: ClientTicks, action: () -> Unit): Task {
        return delay.queue.schedule(delay.value.toLong(), action)
    }

    fun schedule(delay: ServerTicks, action: () -> Unit): Task {
        return server.schedule(delay.value.toLong(), action)
    }

    fun schedule(delay: Duration, action: () -> Unit): Task {
        return TimerTask(executor.schedule({ safely { action() } }, delay.inWholeMilliseconds, TimeUnit.MILLISECONDS))
    }

    fun repeat(interval: ClientTicks, delay: ClientTicks = interval, action: () -> Unit): Task {
        require((interval.packed and 1L) == (delay.packed and 1L)) { "Delay phase must match interval phase" }
        return interval.queue.repeat(interval.value.toLong(), delay.value.toLong(), action)
    }

    fun repeat(interval: ServerTicks, delay: ServerTicks = interval, action: () -> Unit): Task {
        return server.repeat(interval.value.toLong(), delay.value.toLong(), action)
    }

    fun repeat(interval: Duration, delay: Duration = interval, action: () -> Unit): Task {
        return TimerTask(executor.scheduleAtFixedRate(action, delay.inWholeMilliseconds, interval.inWholeMilliseconds, TimeUnit.MILLISECONDS))
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