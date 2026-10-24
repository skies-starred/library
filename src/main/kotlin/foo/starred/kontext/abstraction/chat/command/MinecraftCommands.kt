package foo.starred.kontext.abstraction.chat.command

import foo.starred.kontext.abstraction.client.extension.client
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket

object MinecraftCommands {
    fun send(content: String, client: Boolean = true) {
        if (client) {
            client(content)
            return
        }

        server(content)
    }

    fun client(content: String) {
        client.connection?.sendCommand(content.removePrefix("/"))
    }

    fun server(content: String) {
        client.connection?.send(ServerboundChatCommandPacket(content.removePrefix("/")))
    }
}
