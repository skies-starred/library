package foo.starred.snowbird.api.text.parser.data

import foo.starred.snowbird.api.EMPTY_COMPONENT
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

data class ParserState(val white: Boolean) {
    val stack = ArrayDeque<ParserFrame>()

    var comp: MutableComponent = EMPTY_COMPONENT.copy()
    var current: Int? = null
    var base: Int? = null
    var bold = false
    var italic = false
    var underline = false
    var strikethrough = false
    var obfuscated = false

    fun flush(text: String) {
        if (text.isEmpty()) return
        comp.append(Component.literal(text).apply {
            current?.let { style = style.withColor(it) }

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
