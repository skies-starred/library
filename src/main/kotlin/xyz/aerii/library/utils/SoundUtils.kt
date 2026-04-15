@file:Suppress("Unused")

package xyz.aerii.library.utils

import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import xyz.aerii.library.api.client

@JvmOverloads
fun SoundEvent.play(volume: Float = 1f, pitch: Float = 1f) {
    client.player?.playSound(this, volume, pitch)
}

@JvmOverloads
fun SoundEvent.play(x: Double, y: Double, z: Double, volume: Float = 1f, pitch: Float = 1f) {
    client.soundManager.play(SimpleSoundInstance(this, SoundSource.MASTER, volume, pitch, SoundInstance.createUnseededRandom(), x, y, z))
}

fun String.sound(): SoundEvent? {
    val p = Identifier.tryParse(this) ?: return null
    return SoundEvent.createVariableRangeEvent(p)
}