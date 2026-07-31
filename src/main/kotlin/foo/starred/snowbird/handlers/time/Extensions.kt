@file:Suppress("Unused")

package foo.starred.snowbird.handlers.time

val Int.client
    get() = AbstractChronos.ClientTicks(this)

val Int.server
    get() = AbstractChronos.ServerTicks(this)

val AbstractChronos.ClientTicks.start: AbstractChronos.ClientTicks
    get() = AbstractChronos.ClientTicks(value, true)