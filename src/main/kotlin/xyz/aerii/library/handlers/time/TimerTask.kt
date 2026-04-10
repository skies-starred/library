package xyz.aerii.library.handlers.time

import java.util.concurrent.ScheduledFuture

class TimerTask(private val future: ScheduledFuture<*>) : Task {
    override fun cancel() {
        future.cancel(false)
    }
}