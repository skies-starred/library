package foo.starred.kontext.abstraction.identifier

import foo.starred.kontext.abstraction.client.extension.client
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.Resource
import java.io.InputStream

open class MinecraftIdentifier(val namespace: String) {
    fun resource(path: String): Resource {
        return client.resourceManager.getResource(identify(path)).get()
    }

    fun stream(path: String): InputStream {
        return MinecraftIdentifier::class.java.getResourceAsStream(path) ?: error("Could not find resource: $path")
    }

    fun identify(path: String): Identifier {
        return Identifier.fromNamespaceAndPath(namespace, path)
    }

    fun minecraft(path: String): Identifier {
        return Identifier.withDefaultNamespace(path)
    }

    companion object {
        fun resource(namespace: String, path: String): Resource {
            return client.resourceManager.getResource(identify(namespace, path)).get()
        }

        fun stream(path: String): InputStream {
            return MinecraftIdentifier::class.java.getResourceAsStream(path) ?: error("Could not find resource: $path")
        }

        fun identify(namespace: String, path: String): Identifier {
            return Identifier.fromNamespaceAndPath(namespace, path)
        }

        fun minecraft(path: String): Identifier {
            return Identifier.withDefaultNamespace(path)
        }
    }
}
