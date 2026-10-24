package foo.starred.kontext.api.text.parser.impl

import foo.starred.kontext.abstraction.chat.color.MinecraftColors
import foo.starred.kontext.api.text.parser.data.ParserState
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import java.net.URI

fun String.parse(white: Boolean = false): MutableComponent {
    val state = ParserState(white)
    var start = 0
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
            state.flush(substring(start, i))

            when (lower.drop(1).trim()) {
                "hover", "click" -> state.pop()
                "bold" -> state.bold = false
                "italic" -> state.italic = false
                "underline" -> state.underline = false
                "strikethrough" -> state.strikethrough = false
                "obfuscated" -> state.obfuscated = false
            }

            start = end + 1
            i = end + 1
            continue
        }

        val color0 = if (lower.startsWith('#')) lower.drop(1).toIntOrNull(16) else lower.toIntOrNull()
        val color1 = MinecraftColors.get(lower)?.rgb

        if (lower == "r" || color1 != null || color0 != null) {
            state.flush(substring(start, i))

            val color2 = if (lower == "r") state.base else color1 ?: color0
            state.current = color2

            if (lower != "r" && !state.white && i == 0) {
                state.base = color2
            }

            start = end + 1
            i = end + 1
            continue
        }

        when (lower) {
            "bold" -> {
                state.flush(substring(start, i))
                state.bold = true
                start = end + 1
                i = end + 1
                continue
            }

            "italic" -> {
                state.flush(substring(start, i))
                state.italic = true
                start = end + 1
                i = end + 1
                continue
            }

            "underline" -> {
                state.flush(substring(start, i))
                state.underline = true
                start = end + 1
                i = end + 1
                continue
            }

            "strikethrough" -> {
                state.flush(substring(start, i))
                state.strikethrough = true
                start = end + 1
                i = end + 1
                continue
            }

            "obfuscated" -> {
                state.flush(substring(start, i))
                state.obfuscated = true
                start = end + 1
                i = end + 1
                continue
            }
        }

        if (lower.startsWith("hover:")) {
            state.flush(substring(start, i))
            state.push { it.apply { style = style.withHoverEvent(HoverEvent.ShowText(raw.substringAfter(':').trim().parse())) } }

            start = end + 1
            i = end + 1
            continue
        }

        if (lower.startsWith("click:")) {
            state.flush(substring(start, i))

            val rest = raw.substringAfter(':').trim()
            val colon = rest.indexOf(':')

            if (colon != -1) {
                val type = rest.substring(0, colon).trim().lowercase()
                val value = rest.substring(colon + 1).trim()

                state.push { c ->
                    when (type) {
                        "url" -> c.apply { style = style.withClickEvent(ClickEvent.OpenUrl(URI(value))) }
                        "command" -> c.apply { style = style.withClickEvent(ClickEvent.RunCommand(value)) }
                        "suggest" -> c.apply { style = style.withClickEvent(ClickEvent.SuggestCommand(value)) }
                        else -> c
                    }
                }
            }

            start = end + 1
            i = end + 1
            continue
        }

        i++
    }

    state.flush(substring(start))

    while (state.stack.isNotEmpty()) state.pop()
    return state.comp
}
