package foo.starred.kontext.abstraction.sound

import foo.starred.kontext.abstraction.client.impl.MinecraftClient
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.client.sounds.SoundManager
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource

object MinecraftSound {
    val instance: SoundManager
        get() = MinecraftClient.instance.soundManager

    fun play(sound: SoundEvent, volume: Float = 1f, pitch: Float = 1f) {
        MinecraftClient.instance.player?.playSound(sound, volume, pitch)
    }

    fun play(sound: SoundEvent, x: Double, y: Double, z: Double, volume: Float = 1f, pitch: Float = 1f) {
        instance.play(SimpleSoundInstance(sound, SoundSource.MASTER, volume, pitch, SoundInstance.createUnseededRandom(), x, y, z))
    }

    fun tryCreate(identifier: String): SoundEvent? {
        val location = Identifier.tryParse(identifier) ?: return null
        return SoundEvent.createVariableRangeEvent(location)
    }
}
