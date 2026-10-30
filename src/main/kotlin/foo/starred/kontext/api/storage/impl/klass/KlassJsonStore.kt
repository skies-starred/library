package foo.starred.kontext.api.storage.impl.klass

import com.google.gson.Gson
import foo.starred.kontext.Kontext
import foo.starred.kontext.api.storage.io.impl.AtomicStoredFile
import foo.starred.kontext.api.storage.io.base.AbstractStoredFile
import foo.starred.kontext.util.compress
import foo.starred.kontext.util.decompress
import foo.starred.kontext.util.safely
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.lang.reflect.Type

open class KlassJsonStore<T : Any>(
    val name: String,
    val path: String,
    val type: Type,
    val default: () -> T,
    val gson: Gson = Gson(),
    val stored: AbstractStoredFile = AtomicStoredFile(File(FabricLoader.getInstance().configDir.toFile(), "$name/$path.json"))
) {
    var data: T = default()
        protected set

    init {
        load()

        Kontext.callback {
            save()
        }
    }

    fun load(): T {
        safely {
            data = gson.fromJson(stored.read() ?: return default().also { data = it }, type) ?: default()
            return data
        }

        data = default()
        return data
    }

    fun save() {
        safely {
            stored.write(gson.toJson(data, type))
        }
    }

    fun export(): String {
        return gson.toJson(data, type).compress()
    }

    fun import(compressed: String): Boolean {
        return runCatching {
            val json = compressed.decompress()
            val parsed: T = gson.fromJson(json, type) ?: return false

            data = parsed
            save()
            true
        }.getOrDefault(false)
    }
}
