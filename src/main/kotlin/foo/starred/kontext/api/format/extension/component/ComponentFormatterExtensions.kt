@file:Suppress("UNUSED")

package foo.starred.kontext.api.format.extension.component

import foo.starred.kontext.api.format.impl.component.ComponentFormatter
import foo.starred.kontext.api.text.parser.impl.parse
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.util.FormattedCharSequence

fun String.literal(init: MutableComponent.() -> Unit = {}): MutableComponent {
    return ComponentFormatter.literal(this, init)
}

fun Component.colored(): String {
    return ComponentFormatter.colored(this)
}

fun String.formattedText(): FormattedCharSequence {
    return parse().visualOrderText
}

fun Collection<String>.formattedText(): List<FormattedCharSequence> {
    return map { it.formattedText() }
}
