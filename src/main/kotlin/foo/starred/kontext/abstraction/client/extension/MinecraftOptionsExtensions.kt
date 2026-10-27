@file:Suppress("NOTHING_TO_INLINE")

package foo.starred.kontext.abstraction.client.extension

import com.mojang.blaze3d.platform.InputConstants
import foo.starred.kontext.abstraction.client.option.MinecraftOptions
import net.minecraft.client.KeyMapping

inline fun KeyMapping.boundKey(): InputConstants.Key {
    return MinecraftOptions.findBound(this)
}
