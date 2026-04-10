@file:Suppress("Unused")

package xyz.aerii.library.utils

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import java.util.Optional

private val colorToFormat: Map<TextColor, ChatFormatting> =
    ChatFormatting.entries.mapNotNull { f -> TextColor.fromLegacyFormat(f)?.let { it to f } }.toMap()

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
    style.color?.let { colorToFormat[it] }?.let { append("§${it.char}") }
    if (style.isBold) append("§l")
    if (style.isItalic) append("§o")
    if (style.isUnderlined) append("§n")
    if (style.isStrikethrough) append("§m")
    if (style.isObfuscated) append("§k")
}