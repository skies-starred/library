package foo.starred.kontext.abstraction.chat.generic

import foo.starred.kontext.abstraction.client.extension.client
import foo.starred.kontext.abstraction.gui.MinecraftGui
import foo.starred.kontext.abstraction.text.measurer.MinecraftFontMeasurer
import net.minecraft.client.gui.components.ChatComponent
import kotlin.math.roundToInt

object MinecraftChat {
    val instance: ChatComponent
        //~ if >= 26.2 'chat' -> 'hud.chat'
        get() = MinecraftGui.instance.hud.chat

    val instanceNullable: ChatComponent?
        //~ if >= 26.2 'chat' -> 'hud?.chat'
        get() = MinecraftGui.instanceNullable?.hud?.chat

    val width: Int
        get() = ChatComponent.getWidth(client.options.chatWidth().get())

    val height: Int
        get() = ChatComponent.getHeight(if (instance.isChatFocused) client.options.chatHeightFocused().get() else client.options.chatHeightUnfocused().get())

    fun repeat(content: String): String {
        return content.repeat(width / MinecraftFontMeasurer.width(content))
    }

    fun center(content: String): String {
        val width = width
        val width1 = MinecraftFontMeasurer.width(content)
        if (width1 >= width) return content

        return " ".repeat(((width - width1) / 2f / MinecraftFontMeasurer.width(" ")).roundToInt()) + content
    }
}
