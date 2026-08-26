package foo.starred.snowbird.api.scheduling.scheduler.data.tasks.impl

import foo.starred.snowbird.api.scheduling.scheduler.data.tasks.base.SchedulerTask

class SchedulerTickTask(val action: () -> Unit, val interval: Long? = null) : SchedulerTask {
    var cancelled = false

    override fun cancel() {
        cancelled = true
    }
}
