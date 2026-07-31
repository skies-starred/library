@file:Suppress("Unused")

package foo.starred.snowbird.utils

import foo.starred.snowbird.api.EMPTY_COMPONENT
import foo.starred.snowbird.api.mainThread
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents

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
    //~ if >= 26.2 'gui.' -> 'gui.hud.' {
    gui.hud.setTimes(fadeIn, stay, fadeOut)
    gui.hud.setTitle(this@showTitle)
    gui.hud.setSubtitle(subTitle ?: EMPTY_COMPONENT)
    //~ }
}