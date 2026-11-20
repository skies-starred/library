@file:Suppress("Unused", "ClassName")

package foo.starred.kontext.api.entity.resolver.impl

import foo.starred.kontext.api.entity.resolver.base.LambdaKontextEntityDataResolver
import foo.starred.kontext.api.format.extension.string.stripped
import foo.starred.kontext.core.annotations.loader.LoadableKontextModule

@LoadableKontextModule
object GenericKontextEntityDataResolver {
    object NAME : LambdaKontextEntityDataResolver<String>({ entity ->
        entity.name.stripped()
    }, true)

    object CUSTOM_NAME : LambdaKontextEntityDataResolver<String>({ entity ->
        entity.customName?.stripped()
    }, true)
}
