package foo.starred.snowbird

import com.google.gson.Gson
import foo.starred.snowbird.internal.events.dispatcher.EventDispatcher
import foo.starred.snowbird.internal.misc.DonatorSize
import foo.starred.snowbird.internal.misc.DonatorWords
import foo.starred.kommand.scopes.KommandCommandScope
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Snowbird : ClientModInitializer {
    @JvmField
    val GSON: Gson = Gson()

    @JvmField
    val SCOPE: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineName("Snowbird"))

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(Snowbird::class.java)

    @JvmField
    val COMMANDS: KommandCommandScope<FabricClientCommandSource> = KommandCommandScope()

    override fun onInitializeClient() {
        LOGGER.info("Snowbird initialising...")

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            COMMANDS.register(dispatcher)
        }

        LOGGER.debug("Initialised EventDispatcher - {}", EventDispatcher)
        LOGGER.debug("Initialised CommandLoader - {}", COMMANDS)
        LOGGER.debug("Initialised DonatorWords - {}", DonatorWords)
        LOGGER.debug("Initialised DonatorSize - {}", DonatorSize)
        LOGGER.info("Snowbird finished initialisation.")
    }
}
