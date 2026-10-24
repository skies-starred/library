@file:Suppress("UNUSED", "UNCHECKED_CAST")

package foo.starred.kontext.core.event.impl

import foo.starred.kontext.core.event.data.ScoreboardUpdateEvent
import foo.starred.kontext.core.event.data.TabListUpdateEvent
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

object KontextEvents {
    object Scoreboard {
        val UPDATE = event<ScoreboardUpdateEvent>()
    }

    object TabList {
        val UPDATE = event<TabListUpdateEvent>()
    }

    @JvmName("typedEvent")
    private fun <T> event(): Event<(T) -> Unit> {
        return EventFactory.createArrayBacked(Function1::class.java as Class<(T) -> Unit>) { listeners ->
            { data ->
                for (l in listeners) {
                    l.invoke(data)
                }
            }
        }
    }

    @JvmName("genericEvent")
    private fun event(): Event<() -> Unit> {
        return EventFactory.createArrayBacked(Function0::class.java as Class<() -> Unit>) { listeners ->
            {
                for (l in listeners) {
                    (l as Function0<*>).invoke()
                }
            }
        }
    }
}
