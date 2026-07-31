@file:Suppress("Unused")

package foo.starred.snowbird.api

import foo.starred.snowbird.utils.literal
import foo.starred.snowbird.utils.send
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket
import kotlin.math.roundToInt

fun String.message() {
    client.connection?.sendChat(this)
}

fun String.lie() {
    //~ if >= 26.1 'addMessage' -> 'addClientSystemMessage'
    //~ if >= 26.2 'chat' -> 'hud?.chat'
    mainThread { gui?.hud?.chat?.addClientSystemMessage(this@lie.literal()) }
}

fun Component.lie() {
    //~ if >= 26.1 'addMessage' -> 'addClientSystemMessage'
    //~ if >= 26.2 'chat' -> 'hud?.chat'
    mainThread { gui?.hud?.chat?.addClientSystemMessage(this@lie) }
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