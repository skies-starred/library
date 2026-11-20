@file:Suppress("NOTHING_TO_INLINE")

package foo.starred.kontext.api.entity.extension

import foo.starred.kontext.api.entity.resolver.base.IKontextEntityDataResolver
import net.minecraft.world.entity.Entity

inline fun <reified R : IKontextEntityDataResolver<T>, T> Entity.get(): T? {
    return R::class.objectInstance?.get(this)
}

inline fun <reified R : IKontextEntityDataResolver<T>, T> Entity.getOrDefault(default: T): T {
    return get<R, T>() ?: default
}

inline fun <reified R : IKontextEntityDataResolver<T>, T> Entity.set(value: T?) {
    R::class.objectInstance?.set(this, value)
}

inline fun <reified R : IKontextEntityDataResolver<*>> Entity.has(): Boolean {
    return R::class.objectInstance?.get(this) != null
}

inline fun <reified R : IKontextEntityDataResolver<*>> Entity.invalidate() {
    R::class.objectInstance?.invalidate(this)
}

inline fun <T> Entity.get(resolver: IKontextEntityDataResolver<T>): T? {
    return resolver.get(this)
}

inline fun <T> Entity.getOrDefault(resolver: IKontextEntityDataResolver<T>, default: T): T {
    return resolver.get(this) ?: default
}

inline fun <T> Entity.set(resolver: IKontextEntityDataResolver<T>, value: T?) {
    resolver.set(this, value)
}

inline fun Entity.has(resolver: IKontextEntityDataResolver<*>): Boolean {
    return resolver.get(this) != null
}

inline fun Entity.invalidate(resolver: IKontextEntityDataResolver<*>) {
    resolver.invalidate(this)
}
