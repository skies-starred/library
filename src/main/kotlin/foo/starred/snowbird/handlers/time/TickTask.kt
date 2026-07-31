package foo.starred.snowbird.handlers.time

class TickTask(val action: () -> Unit, val interval: Long? = null) : Task {
    var cancelled = false

    override fun cancel() {
        cancelled = true
    }
}