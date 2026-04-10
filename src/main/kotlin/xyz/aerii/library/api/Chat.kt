@file:Suppress("Unused")

package xyz.aerii.library.api

import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket
import xyz.aerii.library.utils.literal
import xyz.aerii.library.utils.send
import kotlin.math.roundToInt

fun String.message() {
    client.connection?.sendChat(this)
}

fun String.lie() {
    //~ if >= 26.1 'addMessage' -> 'addClientSystemMessage'
    mainThread { gui?.chat?.addClientSystemMessage(this@lie.literal()) }
}

fun Component.lie() {
    //~ if >= 26.1 'addMessage' -> 'addClientSystemMessage'
    mainThread { gui?.chat?.addClientSystemMessage(this@lie) }
}

fun String.command(bool: Boolean = true) {
    if (bool) {
        client.connection?.sendCommand(removePrefix("/"))
        return
    }

    ServerboundChatCommandPacket(removePrefix("/")).send()
}

fun String.repeat(): String {
    return repeat(chatWidth / client.font.width(this))
}

fun String.center(): String {
    val width = chatWidth
    val width1 = client.font.width(this)
    if (width1 >= width) return this

    return " ".repeat(((width - width1) / 2f / client.font.width(" ")).roundToInt()) + this
}