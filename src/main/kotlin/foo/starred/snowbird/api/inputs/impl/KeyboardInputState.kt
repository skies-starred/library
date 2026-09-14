package foo.starred.snowbird.api.inputs.impl

import com.mojang.blaze3d.platform.InputConstants
import foo.starred.snowbird.api.inputs.base.IInputState

//? <= 26.2
//import foo.starred.snowbird.api.client

object KeyboardInputState : IInputState {
    override fun pressed(key: InputConstants.Key): Boolean {
        //~ if >= 26.3 'isKeyDown(client.window, key.value)' -> 'isKeyDown(key.value)'
        return InputConstants.isKeyDown(key.value)
    }

    fun pressed(int: Int): Boolean {
        //~ if >= 26.3 'isKeyDown(client.window, int)' -> 'isKeyDown(int)'
        return InputConstants.isKeyDown(int)
    }
}
