package xyz.aerii.library.handlers.parser

import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import xyz.aerii.library.utils.colors
import java.net.URI

fun String.parse(whiteBase: Boolean = false): MutableComponent {
    val state = ParserState(whiteBase)
    var textStart = 0
    var i = 0

    while (i < length) {
        if (this[i] != '<') {
            i++
            continue
        }

        val end = run {
            var d = 1

            for (j in i + 1 until length) {
                when (this[j]) {
                    '<' -> d++
                    '>' -> if (--d == 0) return@run j
                }
            }

            -1
        }

        if (end == -1) {
            i++
            continue
        }

        val raw = substring(i + 1, end).trim()
        val lower = raw.lowercase()

        if (lower.startsWith('/')) {
            state.flush(substring(textStart, i))

            when (lower.drop(1).trim()) {
                "hover", "click" -> state.pop()
                "bold" -> state.bold = false
                "italic" -> state.italic = false
                "underline" -> state.underline = false
                "strikethrough" -> state.strikethrough = false
                "obfuscated" -> state.obfuscated = false
            }

            textStart = end + 1
            i = end + 1
            continue
        }

        val numericColor = if (lower.startsWith('#')) lower.drop(1).toIntOrNull(16) else lower.toIntOrNull()
        val namedColor = colors[lower]

        if (lower == "r" || namedColor != null || numericColor != null) {
            state.flush(substring(textStart, i))

            val colorVal = if (lower == "r") state.base else namedColor ?: numericColor
            state.current = colorVal

            if (lower != "r" && !state.whiteBase && i == 0) state.base = colorVal

            textStart = end + 1
            i = end + 1
            continue
        }

        when (lower) {
            "bold" -> {
                state.flush(substring(textStart, i))
                state.bold = true
                textStart = end + 1
                i = end + 1
                continue
            }

            "italic" -> {
                state.flush(substring(textStart, i))
                state.italic = true
                textStart = end + 1
                i = end + 1
                continue
            }

            "underline" -> {
                state.flush(substring(textStart, i))
                state.underline = true
                textStart = end + 1
                i = end + 1
                continue
            }

            "strikethrough" -> {
                state.flush(substring(textStart, i))
                state.strikethrough = true
                textStart = end + 1
                i = end + 1
                continue
            }

            "obfuscated" -> {
                state.flush(substring(textStart, i))
                state.obfuscated = true
                textStart = end + 1
                i = end + 1
                continue
            }
        }

        if (lower.startsWith("hover:")) {
            state.flush(substring(textStart, i))
            state.push { it.apply { style = style.withHoverEvent(HoverEvent.ShowText(raw.substringAfter(':').trim().parse())) } }

            textStart = end + 1
            i = end + 1
            continue
        }

        if (lower.startsWith("click:")) {
            state.flush(substring(textStart, i))

            val rest = raw.substringAfter(':').trim()
            val colonIdx = rest.indexOf(':')

            if (colonIdx != -1) {
                val type = rest.substring(0, colonIdx).trim().lowercase()
                val value = rest.substring(colonIdx + 1).trim()

                state.push { c ->
                    when (type) {
                        "url" -> c.apply { style = style.withClickEvent(ClickEvent.OpenUrl(URI(value))) }
                        "command" -> c.apply { style = style.withClickEvent(ClickEvent.RunCommand(value)) }
                        "suggest" -> c.apply { style = style.withClickEvent(ClickEvent.SuggestCommand(value)) }
                        else -> c
                    }
                }
            }

            textStart = end + 1
            i = end + 1
            continue
        }

        i++
    }

    state.flush(substring(textStart))

    while (state.stack.isNotEmpty()) state.pop()
    return state.comp
}