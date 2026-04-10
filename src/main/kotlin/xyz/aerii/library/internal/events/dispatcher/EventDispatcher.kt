package xyz.aerii.library.internal.events.dispatcher

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import xyz.aerii.library.internal.events.GameEvent

object EventDispatcher {
    init {
        ClientLifecycleEvents.CLIENT_STARTED.register {
            GameEvent.Start.post()
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register {
            GameEvent.Stop.post()
        }
    }
}