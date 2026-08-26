package foo.starred.snowbird.api.storage

import foo.starred.snowbird.api.client
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.Resource
import java.io.InputStream

open class AbstractResourceful(val namespace: String) {
    fun resource(path: String): Resource {
        return client.resourceManager.getResource(identify(path)).get()
    }

    fun stream(path: String): InputStream {
        return AbstractResourceful::class.java.getResourceAsStream(path) ?: error("Could not find resource: $path")
    }

    fun identify(path: String): Identifier {
        return Identifier.fromNamespaceAndPath(namespace, path)
    }

    fun minecraft(path: String): Identifier {
        return Identifier.withDefaultNamespace(path)
    }
}
