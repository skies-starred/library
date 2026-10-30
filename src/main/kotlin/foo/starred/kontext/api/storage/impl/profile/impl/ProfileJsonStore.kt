package foo.starred.kontext.api.storage.impl.profile.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import foo.starred.kontext.Kontext
import foo.starred.kontext.api.storage.impl.profile.data.ProfileContainer
import foo.starred.kontext.api.storage.io.impl.AtomicStoredFile
import foo.starred.kontext.api.storage.io.base.AbstractStoredFile
import foo.starred.kontext.util.compress
import foo.starred.kontext.util.decompress
import foo.starred.kontext.util.safely
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.lang.reflect.Type

open class ProfileJsonStore<T : Any>(
    val name: String,
    val path: String,
    val type: Type,
    val default: () -> T,
    val gson: Gson = Gson(),
    val stored: AbstractStoredFile = AtomicStoredFile(File(FabricLoader.getInstance().configDir.toFile(), "$name/$path.json"))
) {
    val containerType: Type = TypeToken.getParameterized(ProfileContainer::class.java, type).type
    var container: ProfileContainer<T> = ProfileContainer("Default", mutableMapOf("Default" to default()))

    val profiles: MutableMap<String, T>
        get() = container.profiles

    val current: T
        get() = profiles.getOrPut(active) { default() }

    var active: String
        get() = container.active
        set(value) {
            container.active = value
            save()
        }

    init {
        load()

        Kontext.callback {
            save()
        }
    }

    fun load() {
        val text = stored.read() ?: return

        safely {
            val parsed: ProfileContainer<T> = gson.fromJson(text, containerType) ?: return@safely
            if (parsed.profiles.isEmpty()) return@safely

            container = parsed
        }
    }

    fun save() {
        safely {
            stored.write(gson.toJson(container, containerType))
        }
    }

    fun select(target: String) {
        if (target !in profiles) profiles[target] = default()
        active = target
    }

    fun add(target: String, item: T = default()): Boolean {
        if (target.isBlank()) return false
        if (target in profiles) return false

        profiles[target] = item
        select(target)
        return true
    }

    fun delete(target: String): Boolean {
        if (profiles.size <= 1) return false
        profiles.remove(target) ?: return false

        if (active == target) {
            active = profiles.keys.first()
        }

        save()
        return true
    }

    fun rename(old: String, new: String): Boolean {
        if (new.isBlank()) return false
        if (new in profiles) return false
        val item = profiles.remove(old) ?: return false

        profiles[new] = item
        if (active == old) {
            active = new
        }

        save()
        return true
    }

    fun export(): String {
        return gson.toJson(mapOf("name" to active, "data" to current)).compress()
    }

    fun import(compressed: String): String? {
        return runCatching {
            val json = compressed.decompress()
            val map: Map<String, Any> = gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
            val name = map["name"] as? String ?: return null
            val raw = gson.toJson(map["data"])
            val imported: T = gson.fromJson(raw, type) ?: return null

            var target = name
            var index = 1
            while (target in profiles) {
                target = "$name ${++index}"
            }

            profiles[target] = imported
            select(target)
            target
        }.getOrNull()
    }
}
