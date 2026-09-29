package foo.starred.snowbird.internal.events

import foo.starred.kbus.data.event.base.KBusEvent

internal sealed class GameEvent {
    internal data object Start : KBusEvent()

    internal data object Stop : KBusEvent()
}
