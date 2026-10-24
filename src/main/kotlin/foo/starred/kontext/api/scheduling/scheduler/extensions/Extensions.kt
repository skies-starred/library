@file:Suppress("Unused")

package foo.starred.kontext.api.scheduling.scheduler.extensions

import foo.starred.kontext.api.scheduling.scheduler.impl.AbstractScheduler

val Int.clientTicks
    get() = AbstractScheduler.ClientTicks(this)

val Int.serverTicks
    get() = AbstractScheduler.ServerTicks(this)

val AbstractScheduler.ClientTicks.start: AbstractScheduler.ClientTicks
    get() = AbstractScheduler.ClientTicks(value, true)

val AbstractScheduler.ClientTicks.end: AbstractScheduler.ClientTicks
    get() = AbstractScheduler.ClientTicks(value, false)
