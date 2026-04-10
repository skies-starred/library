@file:Suppress("Unused")

package xyz.aerii.library.api

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.ChatComponent

val client: Minecraft =
    Minecraft.getInstance()

val font: Font
    get() = client.font

val chatWidth: Int
    get() = ChatComponent.getWidth(client.options.chatWidth().get())

val chatHeight: Int
    get() = ChatComponent.getHeight(if (client.gui.chat.isChatFocused) client.options.chatHeightFocused().get() else client.options.chatHeightUnfocused().get())

inline fun mainThread(crossinline block: Minecraft.() -> Unit) {
    client.execute { client.block() }
}

inline fun nextTick(crossinline block: Minecraft.() -> Unit) {
    client.schedule { client.block() }
}