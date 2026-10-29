@file:Suppress("NOTHING_TO_INLINE")

package foo.starred.kontext.api.item.extension

import foo.starred.kontext.api.item.resolver.base.IKontextItemDataResolver
import net.minecraft.world.item.ItemStack

inline fun <reified R : IKontextItemDataResolver<T>, T> ItemStack.get(): T? {
    return R::class.objectInstance?.get(this)
}

inline fun <reified R : IKontextItemDataResolver<T>, T> ItemStack.getOrDefault(default: T): T {
    return get<R, T>() ?: default
}

inline fun <reified R : IKontextItemDataResolver<T>, T> ItemStack.set(value: T?) {
    R::class.objectInstance?.set(this, value)
}

inline fun <reified R : IKontextItemDataResolver<*>> ItemStack.has(): Boolean {
    return R::class.objectInstance?.get(this) != null
}

inline fun <reified R : IKontextItemDataResolver<*>> ItemStack.invalidate() {
    R::class.objectInstance?.invalidate(this)
}

inline fun <T> ItemStack.get(resolver: IKontextItemDataResolver<T>): T? {
    return resolver.get(this)
}

inline fun <T> ItemStack.getOrDefault(resolver: IKontextItemDataResolver<T>, default: T): T {
    return resolver.get(this) ?: default
}

inline fun <T> ItemStack.set(resolver: IKontextItemDataResolver<T>, value: T?) {
    resolver.set(this, value)
}

inline fun ItemStack.has(resolver: IKontextItemDataResolver<*>): Boolean {
    return resolver.get(this) != null
}

inline fun ItemStack.invalidate(resolver: IKontextItemDataResolver<*>) {
    resolver.invalidate(this)
}
