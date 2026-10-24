@file:Suppress("Unused")

package foo.starred.kontext.util

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import kotlin.jvm.optionals.getOrNull

val JsonElement.asJsonObjectOrNull: JsonObject?
    get() = try {
        asJsonObject
    } catch (_: IllegalStateException) {
        null
    }

fun <T : Any> JsonElement?.deserialize(codec: Codec<T>): T? {
    return codec.parse(JsonOps.INSTANCE, this).result().getOrNull()
}

fun <T : Any> T.serialize(codec: Codec<T>): JsonElement? {
    return codec.encodeStart(JsonOps.INSTANCE, this).result().getOrNull()
}
