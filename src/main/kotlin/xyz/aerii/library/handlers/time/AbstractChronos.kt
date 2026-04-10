@file:Suppress("Unused")

package xyz.aerii.library.handlers.time

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

open class AbstractChronos {
    private val client = TickQueue()
    private val server = TickQueue()
    private val executor = Executors.newSingleThreadScheduledExecutor { Thread(it, "solstice-time").apply { isDaemon = true } }

    val ticks = Ticks()

    fun client() {
        ticks.client++
        client.tick()
    }

    fun server() {
        ticks.server++
        server.tick()
    }

    fun schedule(delay: ClientTicks, action: () -> Unit): Task {
        return client.schedule(delay.value.toLong(), action)
    }

    fun schedule(delay: ServerTicks, action: () -> Unit): Task {
        return server.schedule(delay.value.toLong(), action)
    }

    fun schedule(delay: Duration, action: () -> Unit): Task {
        return TimerTask(executor.schedule(action, delay.inWholeMilliseconds, TimeUnit.MILLISECONDS))
    }

    fun repeat(interval: ClientTicks, delay: ClientTicks = interval, action: () -> Unit): Task {
        return client.repeat(interval.value.toLong(), delay.value.toLong(), action)
    }

    fun repeat(interval: ServerTicks, delay: ServerTicks = interval, action: () -> Unit): Task {
        return server.repeat(interval.value.toLong(), delay.value.toLong(), action)
    }

    fun repeat(interval: Duration, delay: Duration = interval, action: () -> Unit): Task {
        return TimerTask(executor.scheduleAtFixedRate(action, delay.inWholeMilliseconds, interval.inWholeMilliseconds, TimeUnit.MILLISECONDS))
    }

    @JvmInline
    value class ClientTicks(val value: Int)

    @JvmInline
    value class ServerTicks(val value: Int)
}