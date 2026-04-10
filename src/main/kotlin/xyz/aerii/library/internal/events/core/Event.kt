@file:Suppress("Unused")

package xyz.aerii.library.internal.events.core

internal abstract class Event {
    var isCancelled = false
        private set

    fun post(): Boolean {
        EventBus.post(this)
        return isCancelled
    }

    interface Cancellable {
        fun cancel() {
            (this as Event).isCancelled = true
        }
    }
}

internal abstract class CancellableEvent : Event(), Event.Cancellable