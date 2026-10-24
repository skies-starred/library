package foo.starred.kontext.api.scheduling.scheduler.data.tasks.impl

import foo.starred.kontext.api.scheduling.scheduler.data.tasks.base.SchedulerTask
import java.util.concurrent.ScheduledFuture

class SchedulerTimerTask(private val future: ScheduledFuture<*>) : SchedulerTask {
    override fun cancel() {
        future.cancel(false)
    }
}
