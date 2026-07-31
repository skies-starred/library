@file:Suppress("Unused")

package foo.starred.snowbird.handlers

import foo.starred.snowbird.api.client
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.Resource

open class Resourceful(val modId: String) {
    fun resource(path: String): Resource {
        return client.resourceManager.getResource(identify(path)).get()
    }

    fun identify(path: String): Identifier =
        Identifier.fromNamespaceAndPath(modId, path)
}