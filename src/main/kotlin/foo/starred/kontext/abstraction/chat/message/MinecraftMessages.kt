@file:Suppress("UNNECESSARY_SAFE_CALL")

package foo.starred.kontext.abstraction.chat.message

import foo.starred.kontext.abstraction.chat.generic.MinecraftChat
import foo.starred.kontext.abstraction.client.extension.client
import foo.starred.kontext.abstraction.client.extension.mainThread
import foo.starred.kontext.api.text.parser.impl.parse
import net.minecraft.network.chat.Component

object MinecraftMessages {
    fun send(content: String) {
        client.connection?.sendChat(content)
    }

    fun display(message: String) {
        mainThread {
            MinecraftChat.instanceNullable?.addClientSystemMessage(message.parse())
        }
    }

    fun display(message: Component) {
        mainThread {
            MinecraftChat.instanceNullable?.addClientSystemMessage(message)
        }
    }
}
