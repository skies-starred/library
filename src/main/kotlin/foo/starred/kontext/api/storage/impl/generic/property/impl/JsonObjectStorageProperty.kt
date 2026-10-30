package foo.starred.kontext.api.storage.impl.generic.property.impl

import com.google.gson.JsonObject
import foo.starred.kontext.api.storage.impl.generic.impl.GenericJsonStore
import foo.starred.kontext.api.storage.impl.generic.property.base.AbstractStorageProperty
import kotlin.reflect.KProperty

class JsonObjectStorageProperty(
    val store: GenericJsonStore,
    val key: String,
    val default: JsonObject
) : AbstractStorageProperty<JsonObject> {
    override var value: JsonObject
        get() {
            val json = store.load()

            if (!json.has(key)) {
                json.add(key, default)
                store.dirty()
            }

            return json.getAsJsonObject(key)
        }
        set(value) {
            store.load().add(key, value)
            store.dirty()
        }

    override fun getValue(thisRef: Any?, property: KProperty<*>): JsonObject {
        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: JsonObject) {
        this.value = value
    }

    override fun update(block: JsonObject.() -> Unit) {
        val current = value
        current.block()
        value = current
    }
}

