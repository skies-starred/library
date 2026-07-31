package foo.starred.snowbird.internal.events.dispatcher

import foo.starred.snowbird.internal.events.GameEvent
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents

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