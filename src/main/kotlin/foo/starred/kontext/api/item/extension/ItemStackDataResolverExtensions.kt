package foo.starred.kontext.api.item.extension

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import foo.starred.kontext.api.item.resolver.base.IKontextItemDataResolver
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ResolvableProfile
import java.util.*

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

fun ItemStack.texture(texture: String): ItemStack {
    if (item == Items.PLAYER_HEAD) {
        set(DataComponents.PROFILE, ResolvableProfile.createResolved(GameProfile(UUID.randomUUID(), "").apply { properties.put("textures", Property("textures", texture)) }))
    }

    return this
}
