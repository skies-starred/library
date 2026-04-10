package xyz.aerii.library.handlers.parser

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import xyz.aerii.library.api.EMPTY_COMPONENT

class ParserState(val whiteBase: Boolean) {
    var comp: MutableComponent = EMPTY_COMPONENT.copy()
    var current: Int? = null
    var base: Int? = null
    var bold = false
    var italic = false
    var underline = false
    var strikethrough = false
    var obfuscated = false
    val stack = ArrayDeque<ParserFrame>()

    fun flush(text: String) {
        if (text.isEmpty()) return
        comp.append(Component.literal(text).apply {
            current?.let { style.withColor(it) }

            style = style
                .withBold(if (bold) true else null)
                .withItalic(if (italic) true else null)
                .withUnderlined(if (underline) true else null)
                .withStrikethrough(if (strikethrough) true else null)
                .withObfuscated(if (obfuscated) true else null)
        })
    }

    fun push(action: (MutableComponent) -> MutableComponent) {
        stack.addLast(ParserFrame(comp, action))
        comp = EMPTY_COMPONENT.copy()
    }

    fun pop() {
        val frame = stack.removeLast()
        val built = frame.action(comp)
        comp = frame.component
        comp.append(built)
    }
}