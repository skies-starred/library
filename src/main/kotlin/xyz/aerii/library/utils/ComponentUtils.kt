@file:Suppress("Unused")

package xyz.aerii.library.utils

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import java.util.Optional

private val colorToChar: Map<Int, Char> = mapOf(
    0x000000 to '0',
    0x0000AA to '1',
    0x00AA00 to '2',
    0x00AAAA to '3',
    0xAA0000 to '4',
    0xAA00AA to '5',
    0xFFAA00 to '6',
    0xAAAAAA to '7',
    0x555555 to '8',
    0x5555FF to '9',
    0x55FF55 to 'a',
    0x55FFFF to 'b',
    0xFF5555 to 'c',
    0xFF55FF to 'd',
    0xFFFF55 to 'e',
    0xFFFFFF to 'f'
)

fun String.literal(init: MutableComponent.() -> Unit = {}): MutableComponent =
    Component.literal(this).apply(init)

fun MutableComponent.append(text: String, init: MutableComponent.() -> Unit = {}): MutableComponent =
    append(text.literal().apply(init))

fun Component.colorCoded(): String {
    val sb = StringBuilder()
    parse(sb)
    for (s in siblings) s.parse(sb)
    return sb.toString()
}

private fun Component.parse(sb: StringBuilder) {
    contents.visit({ style, text ->
        sb.appender(style)
        sb.append(text)
        Optional.empty<Any>()
    }, style)
}

private fun StringBuilder.appender(style: Style) {
    append("§r")
    val a = style.color?.value?.and(0xFFFFFF)?.let(colorToChar::get)
    if (a != null) append("§$a")
    if (style.isBold) append("§l")
    if (style.isItalic) append("§o")
    if (style.isUnderlined) append("§n")
    if (style.isStrikethrough) append("§m")
    if (style.isObfuscated) append("§k")
}