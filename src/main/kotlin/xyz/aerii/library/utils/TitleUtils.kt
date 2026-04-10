@file:Suppress("Unused")

package xyz.aerii.library.utils

import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import xyz.aerii.library.api.EMPTY_COMPONENT
import xyz.aerii.library.api.mainThread

@JvmOverloads
fun String.alert(showTitle: Boolean = true, playSound: Boolean = true, subTitle: String? = null, soundType: SoundEvent = SoundEvents.NOTE_BLOCK_CHIME.value()) {
    if (showTitle) showTitle(subTitle)
    if (playSound) soundType.play()
}

@JvmOverloads
fun Component.alert(showTitle: Boolean = true, playSound: Boolean = true, subTitle: Component? = null, soundType: SoundEvent = SoundEvents.NOTE_BLOCK_CHIME.value()) {
    if (showTitle) showTitle(subTitle)
    if (playSound) soundType.play()
}

@JvmOverloads
fun String.showTitle(subTitle: String? = null, fadeIn: Int = 5, stay: Int = 20, fadeOut: Int = 5) {
    literal().showTitle(subTitle?.literal(), fadeIn, stay, fadeOut)
}

@JvmOverloads
@Suppress("Deprecation")
fun Component.showTitle(subTitle: Component? = null, fadeIn: Int = 5, stay: Int = 20, fadeOut: Int = 5) = mainThread {
    gui.setTimes(fadeIn, stay, fadeOut)
    gui.setTitle(this@showTitle)
    gui.setSubtitle(subTitle ?: EMPTY_COMPONENT)
}