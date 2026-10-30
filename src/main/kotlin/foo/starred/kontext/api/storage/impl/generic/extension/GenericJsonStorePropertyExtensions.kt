package foo.starred.kontext.api.storage.impl.generic.extension

import com.google.gson.JsonObject
import com.mojang.serialization.Codec
import foo.starred.kontext.api.storage.impl.generic.impl.GenericJsonStore
import foo.starred.kontext.api.storage.impl.generic.property.impl.CodecStorageProperty
import foo.starred.kontext.api.storage.impl.generic.property.impl.JsonObjectStorageProperty

fun GenericJsonStore.jsonObject(key: String, default: JsonObject = JsonObject()): JsonObjectStorageProperty {
    return JsonObjectStorageProperty(this, key, default)
}

fun <T : Any> GenericJsonStore.property(key: String, default: T, codec: Codec<T>): CodecStorageProperty<T> {
    return CodecStorageProperty(this, key, default, codec)
}
