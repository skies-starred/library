package foo.starred.snowbird.api.text.parser.data

import net.minecraft.network.chat.MutableComponent

data class ParserFrame(
    val component: MutableComponent,
    val action: (MutableComponent) -> MutableComponent
)
