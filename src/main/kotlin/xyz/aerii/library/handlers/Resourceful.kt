@file:Suppress("Unused")

package xyz.aerii.library.handlers

import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.Resource
import xyz.aerii.library.api.client

open class Resourceful(val modId: String) {
    fun resource(path: String): Resource {
        return client.resourceManager.getResource(identify(path)).get()
    }

    fun identify(path: String): Identifier =
        Identifier.fromNamespaceAndPath(modId, path)
}