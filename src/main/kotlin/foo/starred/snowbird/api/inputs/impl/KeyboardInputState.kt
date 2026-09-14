package foo.starred.snowbird.api.inputs.impl

import com.mojang.blaze3d.platform.InputConstants
import foo.starred.snowbird.api.inputs.base.IInputState

//? <= 26.2
//import foo.starred.snowbird.api.client

object KeyboardInputState : IInputState {
    override fun pressed(key: Int): Boolean {
        //~ if >= 26.3 'isKeyDown(client.window, key)' -> 'isKeyDown(key)'
        return InputConstants.isKeyDown(key)
    }
}
