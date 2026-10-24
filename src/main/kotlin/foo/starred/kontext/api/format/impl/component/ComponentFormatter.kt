package foo.starred.kontext.api.format.impl.component

import foo.starred.kontext.abstraction.chat.color.MinecraftColors
import foo.starred.kontext.api.text.parser.impl.parse
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import java.util.*

object ComponentFormatter {
    fun literal(string: String, init: MutableComponent.() -> Unit = {}): MutableComponent {
        return Component.literal(string).apply(init)
    }

    fun parse(string: String, white: Boolean = false): MutableComponent {
        return string.parse(white)
    }

    fun colored(component: Component): String {
        val builder = StringBuilder()

        parse(component, builder)
        for (component in component.siblings) {
            parse(component, builder)
        }

        return builder.toString()
    }

    private fun parse(component: Component, builder: StringBuilder) {
        component.contents.visit({ style, text ->
            builder.appender(style)
            builder.append(text)
            Optional.empty<Any>()
        }, component.style)
    }

    private fun StringBuilder.appender(style: Style) {
        append("§r")
        val a = style.color?.value?.and(0xFFFFFF)?.let { MinecraftColors.get(it)?.char }
        if (a != null) append("§$a")
        if (style.isBold) append("§l")
        if (style.isItalic) append("§o")
        if (style.isUnderlined) append("§n")
        if (style.isStrikethrough) append("§m")
        if (style.isObfuscated) append("§k")
    }
}
