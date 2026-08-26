package foo.starred.snowbird.utils

import foo.starred.snowbird.api.nextTick
import net.minecraft.client.gui.screens.Screen

fun Screen.open() {
    //~ if >= 26.2 'setScreen(' -> 'gui.setScreen('
    nextTick { gui.setScreen(this@open) }
}
