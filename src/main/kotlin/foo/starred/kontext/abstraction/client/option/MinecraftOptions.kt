package foo.starred.kontext.abstraction.client.option

import com.mojang.blaze3d.platform.InputConstants
import foo.starred.kontext.abstraction.client.impl.MinecraftClient
import foo.starred.kontext.mixin.accessors.KeyMappingAccessor
import net.minecraft.client.KeyMapping
import net.minecraft.client.Options

object MinecraftOptions {
    val instance: Options
        get() = MinecraftClient.instance.options

    val instanceNullable: Options?
        get() = MinecraftClient.instance.options

    fun findBound(key: KeyMapping): InputConstants.Key {
        return (key as KeyMappingAccessor).boundKey()
    }
}
