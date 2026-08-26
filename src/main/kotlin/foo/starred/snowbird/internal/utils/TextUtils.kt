package foo.starred.snowbird.internal.utils

import foo.starred.snowbird.api.lie
import foo.starred.snowbird.api.text.parser.impl.parse
import foo.starred.snowbird.utils.literal
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

private val prefix = "<#7695FF>[<#8BA5FF>S<#A1B6FF>n<#B6C6FF>o<#DBE3FF>w<#FFFFFF>b<#B6C6FF>i<#95ACFE>r<#7492FD>d<#5378FC>]".parse()

internal fun String.mod(block: MutableComponent.() -> Unit = {}) {
    parse(true).apply(block).mod()
}

internal fun Component.mod(block: MutableComponent.() -> Unit = {}) {
    prefix.copy()
        .append(" ".literal())
        .append(if (style.color == null) copy().withColor(0xFFFFFF) else this)
        .apply(block)
        .lie()
}
