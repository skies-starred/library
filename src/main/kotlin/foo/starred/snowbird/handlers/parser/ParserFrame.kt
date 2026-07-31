package foo.starred.snowbird.handlers.parser

import net.minecraft.network.chat.MutableComponent

data class ParserFrame(val component: MutableComponent, val action: (MutableComponent) -> MutableComponent)