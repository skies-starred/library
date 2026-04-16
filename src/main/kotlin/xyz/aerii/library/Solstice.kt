package xyz.aerii.library

import net.fabricmc.api.ClientModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import xyz.aerii.library.internal.misc.DonatorWords
import xyz.aerii.library.internal.events.dispatcher.EventDispatcher

object Solstice : ClientModInitializer {
    @JvmField
    val LOGGER: Logger = LogManager.getLogger(Solstice::class.java)

    override fun onInitializeClient() {
        LOGGER.info("Solstice initialising...")
        LOGGER.debug("Initialised EventDispatcher - {}", EventDispatcher)
        LOGGER.debug("Initialised DonatorWords - {}", DonatorWords)
        LOGGER.info("Solstice finished initialisation.")
    }
}