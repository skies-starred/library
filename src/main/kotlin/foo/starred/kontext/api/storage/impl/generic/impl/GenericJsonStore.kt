@file:Suppress("Unused")

package foo.starred.kontext.api.storage.impl.generic.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import foo.starred.kontext.Kontext
import foo.starred.kontext.api.storage.io.impl.AtomicStoredFile
import foo.starred.kontext.api.storage.io.base.AbstractStoredFile
import foo.starred.kontext.util.asJsonObjectOrNull
import foo.starred.kontext.util.safely
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import kotlin.time.Duration.Companion.seconds

open class GenericJsonStore(
    val name: String,
    val path: String,
    val tts: Int = 5,
    val stored: AbstractStoredFile = AtomicStoredFile(File(FabricLoader.getInstance().configDir.toFile(), "$name/$path.json"))
) {
    val gson: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

    var root: JsonObject? = null
    var job: Job? = null

    var dirty = false
        private set

    init {
        Kontext.callback {
            flush()
        }
    }

    fun load(): JsonObject {
        if (root != null) return root!!

        val text = stored.read()
        if (text.isNullOrBlank()) {
            root = JsonObject()
            return root!!
        }

        root = try {
            JsonParser.parseString(text).asJsonObjectOrNull?.get("@$name:data")?.asJsonObjectOrNull ?: JsonObject()
        } catch (e: Exception) {
            Kontext.LOGGER.error("Failed to parse storage for $name at $path: $e")
            JsonObject()
        }

        return root!!
    }

    fun save() {
        safely {
            val data = root ?: return
            val wrapped = JsonObject().apply { add("@$name:data", data) }
            stored.write(gson.toJson(wrapped))
            reset()
        }
    }

    fun flush() {
        job?.cancel()
        if (!dirty) return

        save()
        dirty = false
    }

    fun dirty() {
        dirty = true
        job?.cancel()
        job = scope.launch {
            delay(tts.seconds)
            flush()
        }
    }

    fun reset() {
        dirty = false
        job?.cancel()
    }

    companion object {
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}
