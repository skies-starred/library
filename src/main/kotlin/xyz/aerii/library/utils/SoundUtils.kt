@file:Suppress("Unused")

package xyz.aerii.library.utils

import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import xyz.aerii.library.api.client

@JvmOverloads
fun SoundEvent.play(volume: Float = 1f, pitch: Float = 1f) {
    client.player?.playSound(this, volume, pitch)
}

fun String.sound(): SoundEvent? {
    val p = Identifier.tryParse(this) ?: return null
    return SoundEvent.createVariableRangeEvent(p)
}