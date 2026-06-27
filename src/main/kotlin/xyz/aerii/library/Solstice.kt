package xyz.aerii.library

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.fabricmc.api.ClientModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import xyz.aerii.library.internal.events.dispatcher.EventDispatcher
import xyz.aerii.library.internal.misc.*
import xyz.aerii.library.kommand.loader.CommandLoader

object Solstice : ClientModInitializer {
    @JvmField
    val GSON: Gson = Gson()

    @JvmField
    val SCOPE: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineName("Solstice"))

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(Solstice::class.java)

    override fun onInitializeClient() {
        LOGGER.info("Solstice initialising...")
        LOGGER.debug("Initialised EventDispatcher - {}", EventDispatcher)
        LOGGER.debug("Initialised CommandLoader - {}", CommandLoader)
        LOGGER.debug("Initialised DonatorWords - {}", DonatorWords)
        LOGGER.debug("Initialised DonatorSize - {}", DonatorSize)
        LOGGER.info("Solstice finished initialisation.")
    }
}