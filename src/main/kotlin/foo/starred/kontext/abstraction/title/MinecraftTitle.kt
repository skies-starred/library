package foo.starred.kontext.abstraction.title

import foo.starred.kontext.abstraction.client.extension.mainThread
import foo.starred.kontext.abstraction.gui.MinecraftGui
import foo.starred.kontext.api.EMPTY_COMPONENT
import foo.starred.kontext.api.format.extension.component.literal
import net.minecraft.network.chat.Component

object MinecraftTitle {
    fun display(title: String, subTitle: String? = null, fadeIn: Int = 5, stay: Int = 20, fadeOut: Int = 5) {
        display(title.literal(), subTitle?.literal(), fadeIn, stay, fadeOut)
    }

    fun display(title: Component, subTitle: Component? = null, fadeIn: Int = 5, stay: Int = 20, fadeOut: Int = 5) {
        mainThread {
            //~ if >= 26.2 'instance.' -> 'instance.hud.' {
            MinecraftGui.instance.hud.setTimes(fadeIn, stay, fadeOut)
            MinecraftGui.instance.hud.setTitle(title)
            MinecraftGui.instance.hud.setSubtitle(subTitle ?: EMPTY_COMPONENT)
            //~ }
        }
    }
}
