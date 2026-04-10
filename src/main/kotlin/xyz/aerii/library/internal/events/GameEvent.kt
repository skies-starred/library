package xyz.aerii.library.internal.events

import xyz.aerii.library.internal.events.core.Event

internal sealed class GameEvent {
    internal data object Start : Event()

    internal data object Stop : Event()
}