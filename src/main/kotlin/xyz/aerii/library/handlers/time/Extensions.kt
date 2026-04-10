@file:Suppress("Unused")

package xyz.aerii.library.handlers.time

val Int.client
    get() = AbstractChronos.ClientTicks(this)

val Int.server
    get() = AbstractChronos.ServerTicks(this)
