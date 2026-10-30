package foo.starred.kontext

import com.google.gson.Gson
import foo.starred.kommand.scopes.KommandCommandScope
import foo.starred.kontext.api.storage.impl.generic.impl.GenericJsonStore
import foo.starred.kontext.core.annotations.impl.KontextAnnotationLoader
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Kontext : ClientModInitializer {
    private val callbacks: MutableList<() -> Unit> = mutableListOf()

    @JvmField
    val GSON: Gson = Gson()

    @JvmField
    val SCOPE: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineName("Kontext"))

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("Kontext")

    @JvmField
    val COMMANDS: KommandCommandScope<FabricClientCommandSource> = KommandCommandScope()

    @JvmField
    val JSON: GenericJsonStore = GenericJsonStore("kontext", "config")

    override fun onInitializeClient() {
        LOGGER.info("Kontext initialising...")

        KontextAnnotationLoader.init()

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            COMMANDS.register(dispatcher)
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register {
            for (callback in callbacks) {
                callback()
            }
        }

        LOGGER.info("Kontext finished initialisation.")
    }

    fun callback(block: () -> Unit) {
        callbacks += block
    }
}
