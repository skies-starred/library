@file:Suppress("Unused")

package xyz.aerii.library.handlers.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mojang.serialization.Codec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.fabricmc.loader.api.FabricLoader
import xyz.aerii.library.Solstice
import xyz.aerii.library.internal.events.GameEvent
import xyz.aerii.library.internal.events.core.on
import xyz.aerii.library.utils.asJsonObjectOrNull
import xyz.aerii.library.utils.deserialize
import xyz.aerii.library.utils.serialize
import java.io.File
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class AbstractScribble(private val name: String, private val path: String, private val tts: Int = 5) {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    private val file: File = File(FabricLoader.getInstance().configDir.toFile(), "$name/$path.json").apply { parentFile.mkdirs() }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var root: JsonObject? = null
    private var dirty = false
    private var job: Job? = null

    init {
        on<GameEvent.Stop> {
            scope.launch {
                job?.cancelAndJoin()
                save()
            }
        }
    }

    private fun load(): JsonObject {
        if (root != null) return root!!

        root = try {
            if (!file.exists() || file.length() <= 0) JsonObject()
            else JsonParser.parseString(file.readText()).asJsonObjectOrNull?.get("@$name:data")?.asJsonObjectOrNull ?: JsonObject()
        } catch (e: Exception) {
            Solstice.LOGGER.error("Failed to load scribbled file for $name at $path: $e")
            JsonObject()
        }

        return root!!
    }

    private fun save() {
        if (!dirty) return

        try {
            val data = root ?: return

            val wrapped = JsonObject().apply {
                add("@$name:data", data)
            }

            val tempFile = File(file.parent, "${file.name}.tmp")
            tempFile.writeText(gson.toJson(wrapped))

            if (file.exists()) file.delete()
            if (!tempFile.renameTo(file)) {
                tempFile.copyTo(file, overwrite = true)
                tempFile.delete()
            }

            dirty = false
        } catch (e: Exception) {}
    }

    fun dirty() {
        dirty = true
        job?.cancel()
        job = scope.launch {
            delay(tts * 1000L)
            save()
        }
    }

    fun jsonObject(key: String, default: JsonObject = JsonObject()) = object : ReadWriteProperty<Any?, JsonObject> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): JsonObject {
            val obj = load()

            if (!obj.has(key)) {
                obj.add(key, default)
                dirty()
            }

            return obj.getAsJsonObject(key)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: JsonObject) {
            load().add(key, value)
            dirty()
        }
    }

    inner class Value<T : Any>(
        private val key: String,
        private val default: T,
        private val codec: Codec<T>
    ) : ReadWriteProperty<Any?, T> {
        private var _v: T? = null

        var value: T
            get() {
                if (_v == null) _v = load().get(key)?.deserialize(codec) ?: default
                return _v!!
            }
            set(value) {
                _v = value

                value.serialize(codec)?.let {
                    load().add(key, it)
                    dirty()
                }
            }

        init {
            val obj = load()

            if (!obj.has(key)) default.serialize(codec)?.let {
                obj.add(key, it)
                dirty()
            }
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return value
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            ::value.set(value)
        }

        fun update(block: T.() -> Unit) {
            value = value.apply(block)
        }
    }

    fun int(key: String, default: Int = 0) = Value(key, default, Codec.INT)
    fun long(key: String, default: Long = 0L) = Value(key, default, Codec.LONG)
    fun string(key: String, default: String = "") = Value(key, default, Codec.STRING)
    fun boolean(key: String, default: Boolean = false) = Value(key, default, Codec.BOOL)
    fun double(key: String, default: Double = 0.0) = Value(key, default, Codec.DOUBLE)
    fun float(key: String, default: Float = 0f) = Value(key, default, Codec.FLOAT)

    fun <T : Any> list(key: String, codec: Codec<T>, default: List<T> = emptyList()) = Value(key, default, codec.listOf())
    fun <T : Any> set(key: String, codec: Codec<T>, default: Set<T> = emptySet()) = Value(key, default, codec.listOf().xmap({ it.toSet() }, { it.toList() }))
    fun <K : Any, V : Any> map(key: String, keyCodec: Codec<K>, valueCodec: Codec<V>, default: Map<K, V> = emptyMap()) = Value(key, default, Codec.unboundedMap(keyCodec, valueCodec))

    fun <T : Any> mutableList(key: String, codec: Codec<T>, default: MutableList<T> = mutableListOf()) = Value(key, default, codec.listOf().xmap({ it.toMutableList() }, { it.toList() }))
    fun <T : Any> mutableSet(key: String, codec: Codec<T>, default: MutableSet<T> = mutableSetOf()) = Value(key, default, codec.listOf().xmap({ it.toMutableSet() }, { it.toList() }))
    fun <K : Any, V : Any> mutableMap(key: String, keyCodec: Codec<K>, valueCodec: Codec<V>, default: MutableMap<K, V> = mutableMapOf()) = Value(key, default, Codec.unboundedMap(keyCodec, valueCodec).xmap({ it.toMutableMap() }, { it.toMap() }))
}