package foo.starred.kontext.api.storage.impl.generic.property.impl

import com.mojang.serialization.Codec
import foo.starred.kontext.api.storage.impl.generic.impl.GenericJsonStore
import foo.starred.kontext.api.storage.impl.generic.property.base.AbstractStorageProperty
import foo.starred.kontext.util.deserialize
import foo.starred.kontext.util.serialize
import kotlin.reflect.KProperty

open class CodecStorageProperty<T : Any>(
    val store: GenericJsonStore,
    val key: String,
    val default: T,
    val codec: Codec<T>
) : AbstractStorageProperty<T> {
    private var cached: T? = null

    init {
        val json = store.load()
        if (!json.has(key)) {
            default.serialize(codec)?.let {
                json.add(key, it)
                store.dirty()
            }
        }
    }

    override var value: T
        get() {
            if (cached == null) cached = store.load().get(key)?.deserialize(codec) ?: default
            return cached!!
        }
        set(value) {
            cached = value
            value.serialize(codec)?.let {
                store.load().add(key, it)
                store.dirty()
            }
        }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    override fun update(block: T.() -> Unit) {
        val current = value
        current.block()
        value = current
    }
}
