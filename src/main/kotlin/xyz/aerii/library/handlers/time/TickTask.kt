package xyz.aerii.library.handlers.time

class TickTask(val action: () -> Unit, val interval: Long? = null) : Task {
    var cancelled = false

    override fun cancel() {
        cancelled = true
    }
}