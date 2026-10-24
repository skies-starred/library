package foo.starred.kontext.core.event.data

import net.minecraft.network.chat.Component

data class TabListUpdateEvent(
    val lines0: List<List<String>>,
    val lines1: List<List<String>>,
    val components0: List<List<Component>>,
    val components1: List<List<Component>>,
)
