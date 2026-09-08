@file:Suppress("Unused")

package foo.starred.snowbird.api

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.ChatComponent

@JvmField
val client: Minecraft =
    Minecraft.getInstance()

@JvmName("getClient2")
@Deprecated("Use client.", ReplaceWith("client"))
fun getClient(): Minecraft {
    return client
}

val font: Font
    get() = client.font

val chatWidth: Int
    get() = ChatComponent.getWidth(client.options.chatWidth().get())

val chatHeight: Int
    //~ if >= 26.2 'gui.chat' -> 'gui.hud.chat'
    get() = ChatComponent.getHeight(if (client.gui.hud.chat.isChatFocused) client.options.chatHeightFocused().get() else client.options.chatHeightUnfocused().get())

inline fun mainThread(crossinline block: Minecraft.() -> Unit) {
    client.execute { client.block() }
}

inline fun nextTick(crossinline block: Minecraft.() -> Unit) {
    client.schedule { client.block() }
}
