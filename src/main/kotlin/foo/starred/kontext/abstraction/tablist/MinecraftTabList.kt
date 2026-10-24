@file:Suppress("UNUSED")

package foo.starred.kontext.abstraction.tablist

import foo.starred.kontext.abstraction.client.impl.MinecraftClient
import foo.starred.kontext.abstraction.gui.MinecraftGui
import foo.starred.kontext.api.format.extension.string.stripped
import foo.starred.kontext.core.annotations.loader.LoadableKontextModule
import foo.starred.kontext.core.event.data.TabListUpdateEvent
import foo.starred.kontext.core.event.impl.KontextEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.client.gui.components.PlayerTabOverlay
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.network.chat.Component
import net.minecraft.world.level.GameType

@LoadableKontextModule
object MinecraftTabList {
    private val comparator: Comparator<PlayerInfo> = compareByDescending<PlayerInfo> { it.tabListOrder }.thenBy { it.gameMode == GameType.SPECTATOR }.thenBy { it.team?.name.orEmpty() }.thenBy(String.CASE_INSENSITIVE_ORDER) { it.profile.name }
    private var ticks = 0

    val instance: PlayerTabOverlay
        //~ if >= 26.2 'instance.tabList' -> 'instance.hud.tabList'
        get() = MinecraftGui.instance.hud.tabList

    val instanceNullable: PlayerTabOverlay?
        //~ if >= 26.2 'instanceNullable?.tabList' -> 'instanceNullable?.hud?.tabList'
        get() = MinecraftGui.instanceNullable?.hud?.tabList

    val unsorted: List<PlayerInfo>
        get() = MinecraftClient.instance.connection?.listedOnlinePlayers?.toList() ?: emptyList()

    val sorted: List<PlayerInfo>
        get() = unsorted.sortedWith(comparator)

    val players: List<PlayerInfo>
        get() = unsorted.filter { it.profile.id().version() == 4 }

    var lines: List<List<String>> = emptyList()
        private set

    var components: List<List<Component>> = emptyList()
        private set

    init {
        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            if (++ticks % 10 != 0) return@register

            val instance = instanceNullable
            if (instance == null) {
                if (components.isNotEmpty()) reset()
                return@register
            }

            val players = sorted
            if (players.isEmpty()) {
                if (components.isNotEmpty()) reset()
                return@register
            }

            val components0 = components
            val lines0 = lines

            val components1 = players.take(80).map { instance.getNameForDisplay(it) }.chunked(20).takeIf { it != components } ?: return@register
            val lines1 = components1.map { a -> a.map { b -> b.stripped() } }

            components = components1
            lines = lines1
            KontextEvents.TabList.UPDATE.invoker().invoke(TabListUpdateEvent(lines0, lines1, components0, components1))
        }

        ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
            reset()
        }

        ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
            reset()
        }
    }

    private fun reset() {
        lines = emptyList()
        components = emptyList()
    }
}
