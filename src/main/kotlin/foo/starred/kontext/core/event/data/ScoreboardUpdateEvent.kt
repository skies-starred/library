package foo.starred.kontext.core.event.data

import net.minecraft.network.chat.Component

data class ScoreboardUpdateEvent(
    val lines0: List<String>,
    val lines1: List<String>,
    val components0: List<Component>,
    val components1: List<Component>,
) {
    val added: List<String> by lazy(LazyThreadSafetyMode.NONE) {
        lines1 - lines0.toSet()
    }

    val removed: List<String> by lazy(LazyThreadSafetyMode.NONE) {
        lines0 - lines1.toSet()
    }
}
