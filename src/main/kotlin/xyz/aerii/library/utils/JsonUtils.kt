@file:Suppress("Unused")

package xyz.aerii.library.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.zip.Deflater
import java.util.zip.Inflater
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

fun String.compress(): String = Base64.getEncoder().encodeToString(
    ByteArrayOutputStream().also { b ->
        val buffer = ByteArray(1024)
        val deflate = Deflater().apply { setInput(this@compress.toByteArray()); finish() }
        while (!deflate.finished()) b.write(buffer, 0, deflate.deflate(buffer))
    }.toByteArray()
)

fun String.decompress(): String =
    ByteArrayOutputStream().also { b ->
        val buffer = ByteArray(1024)
        val inflater = Inflater().apply { setInput(Base64.getDecoder().decode(this@decompress)) }
        while (!inflater.finished()) b.write(buffer, 0, inflater.inflate(buffer))
    }.toString()