@file:Suppress("Unused")

package xyz.aerii.library.utils

import net.minecraft.sounds.SoundEvent
import xyz.aerii.library.api.client

@JvmOverloads
fun SoundEvent.play(volume: Float = 1f, pitch: Float = 1f) {
    client.player?.playSound(this, volume, pitch)
}