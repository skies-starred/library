package foo.starred.snowbird.handlers.parser

import foo.starred.snowbird.api.text.parser.impl.parse
import net.minecraft.network.chat.MutableComponent

@Deprecated("Use foo.starred.snowbird.api.text.parser.impl.parse")
fun String.parse(whiteBase: Boolean = false): MutableComponent {
    return parse(whiteBase)
}
