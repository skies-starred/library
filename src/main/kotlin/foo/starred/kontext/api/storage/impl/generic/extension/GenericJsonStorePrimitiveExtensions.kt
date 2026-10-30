package foo.starred.kontext.api.storage.impl.generic.extension

import com.mojang.serialization.Codec
import foo.starred.kontext.api.storage.impl.generic.impl.GenericJsonStore
import foo.starred.kontext.api.storage.impl.generic.property.impl.CodecStorageProperty

fun GenericJsonStore.int(key: String, default: Int = 0): CodecStorageProperty<Int> {
    return property(key, default, Codec.INT)
}

fun GenericJsonStore.long(key: String, default: Long = 0L): CodecStorageProperty<Long> {
    return property(key, default, Codec.LONG)
}

fun GenericJsonStore.string(key: String, default: String = ""): CodecStorageProperty<String> {
    return property(key, default, Codec.STRING)
}

fun GenericJsonStore.boolean(key: String, default: Boolean = false): CodecStorageProperty<Boolean> {
    return property(key, default, Codec.BOOL)
}

fun GenericJsonStore.double(key: String, default: Double = 0.0): CodecStorageProperty<Double> {
    return property(key, default, Codec.DOUBLE)
}

fun GenericJsonStore.float(key: String, default: Float = 0f): CodecStorageProperty<Float> {
    return property(key, default, Codec.FLOAT)
}
