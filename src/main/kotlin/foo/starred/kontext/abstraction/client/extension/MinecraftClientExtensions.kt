@file:Suppress("Unused")

package foo.starred.kontext.abstraction.client.extension

import foo.starred.kontext.abstraction.client.impl.MinecraftClient
import net.minecraft.client.Minecraft

@JvmField
val client: Minecraft = MinecraftClient.instance

inline fun mainThread(crossinline block: Minecraft.() -> Unit) {
    client.execute {
        client.block()
    }
}

inline fun nextTick(crossinline block: Minecraft.() -> Unit) {
    client.schedule {
        client.block()
    }
}
