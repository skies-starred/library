package foo.starred.kontext.core.util

import foo.starred.kontext.abstraction.chat.message.MinecraftMessages
import foo.starred.kontext.api.format.extension.component.literal
import foo.starred.kontext.api.text.parser.impl.parse
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

private val prefix = "<#61C6EE>[<#72CCF2>K<#82D2F7>o<#93D8FB>n<#A3DEFF>t<#93D8FB>e<#82D2F7>x<#72CCF2>t<#61C6EE>]".parse()

internal fun String.mod(block: MutableComponent.() -> Unit = {}) {
    parse(true).apply(block).mod()
}

internal fun Component.mod(block: MutableComponent.() -> Unit = {}) {
    MinecraftMessages.display(prefix.copy().append(" ".literal()).append(if (style.color == null) copy().withColor(0xFFFFFF) else this).apply(block))
}
