package foo.starred.snowbird.internal.events

import foo.starred.snowbird.internal.events.core.Event

internal sealed class GameEvent {
    internal data object Start : Event()

    internal data object Stop : Event()
}