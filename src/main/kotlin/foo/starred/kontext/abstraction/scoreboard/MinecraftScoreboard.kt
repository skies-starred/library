@file:Suppress("LocalVariableName")

package foo.starred.kontext.abstraction.scoreboard

import foo.starred.kontext.abstraction.client.impl.MinecraftClient
import foo.starred.kontext.api.format.extension.string.stripped
import foo.starred.kontext.core.annotations.loader.LoadableKontextModule
import foo.starred.kontext.core.event.data.ScoreboardUpdateEvent
import foo.starred.kontext.core.event.impl.KontextEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.network.chat.Component
import net.minecraft.world.scores.DisplaySlot
import net.minecraft.world.scores.PlayerTeam
import net.minecraft.world.scores.Scoreboard

@LoadableKontextModule
object MinecraftScoreboard {
    private var ticks = 0

    val instance: Scoreboard?
        get() = MinecraftClient.instance.level?.scoreboard

    var title: Component? = null
        private set

    var lines: List<String> = emptyList()
        private set

    var components: List<Component> = emptyList()
        private set

    init {
        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            if (++ticks % 10 != 0) return@register

            val instance = instance
            if (instance == null) {
                if (components.isNotEmpty()) reset()
                return@register
            }

            val objective = instance.getDisplayObjective(DisplaySlot.SIDEBAR)?.also { title = it.displayName }
            if (objective == null) {
                if (components.isNotEmpty()) reset()
                return@register
            }

            val components0 = components
            val lines0 = lines

            val components1 = instance.listPlayerScores(objective).filter { !it.isHidden }.sortedByDescending { it.value() }.take(15).map { PlayerTeam.formatNameForTeam(instance.getPlayersTeam(it.owner()), it.ownerName()) }.takeIf { it != components } ?: return@register
            val lines1 =  components1.map { it.stripped() }

            components = components1
            lines = lines1
            KontextEvents.Scoreboard.UPDATE.invoker().invoke(ScoreboardUpdateEvent(lines0, lines1, components0, components1))
        }

        ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
            reset()
        }

        ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
            reset()
        }
    }

    private fun reset() {
        title = null
        lines = emptyList()
        components = emptyList()
    }
}
