package foo.starred.kontext.api.storage.impl.generic.extension

import com.mojang.serialization.Codec
import foo.starred.kontext.api.storage.impl.generic.impl.GenericJsonStore
import foo.starred.kontext.api.storage.impl.generic.property.impl.CodecStorageProperty

fun <T : Any> GenericJsonStore.list(key: String, codec: Codec<T>, default: List<T> = emptyList()): CodecStorageProperty<List<T>> {
    return property(key, default, codec.listOf())
}

fun <T : Any> GenericJsonStore.set(key: String, codec: Codec<T>, default: Set<T> = emptySet()): CodecStorageProperty<Set<T>> {
    return property(key, default, codec.listOf().xmap({ it.toSet() }, { it.toList() }))
}

fun <K : Any, V : Any> GenericJsonStore.map(key: String, keyCodec: Codec<K>, valueCodec: Codec<V>, default: Map<K, V> = emptyMap()): CodecStorageProperty<Map<K, V>> {
    return property(key, default, Codec.unboundedMap(keyCodec, valueCodec))
}

fun <T : Any> GenericJsonStore.mutableList(key: String, codec: Codec<T>, default: MutableList<T> = mutableListOf()): CodecStorageProperty<MutableList<T>> {
    return property(key, default, codec.listOf().xmap({ it.toMutableList() }, { it.toList() }))
}

fun <T : Any> GenericJsonStore.mutableSet(key: String, codec: Codec<T>, default: MutableSet<T> = mutableSetOf()): CodecStorageProperty<MutableSet<T>> {
    return property(key, default, codec.listOf().xmap({ it.toMutableSet() }, { it.toList() }))
}

fun <K : Any, V : Any> GenericJsonStore.mutableMap(key: String, keyCodec: Codec<K>, valueCodec: Codec<V>, default: MutableMap<K, V> = mutableMapOf()): CodecStorageProperty<MutableMap<K, V>> {
    return property(key, default, Codec.unboundedMap(keyCodec, valueCodec).xmap({ it.toMutableMap() }, { it.toMap() }))
}
