package foo.starred.kontext.api.storage.impl.generic.property.base

import kotlin.properties.ReadWriteProperty

interface AbstractStorageProperty<T> : ReadWriteProperty<Any?, T> {
    var value: T

    fun update(block: T.() -> Unit)
}

