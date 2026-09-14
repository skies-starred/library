package foo.starred.snowbird.api.inputs.impl

import com.mojang.blaze3d.platform.InputConstants
import foo.starred.snowbird.api.inputs.base.IInputState

object GenericInputState : IInputState {
    override fun pressed(key: Int): Boolean {
        return when {
            !bound(key) -> false
            key > 7 -> KeyboardInputState.pressed(key)
            else -> MouseInputState.pressed(key)
        }
    }

    fun bound(key: Int): Boolean {
        return key != -1
    }

    object States {
        fun shift(): Boolean {
            return KeyboardInputState.pressed(InputConstants.KEY_LSHIFT) || KeyboardInputState.pressed(InputConstants.KEY_RSHIFT)
        }

        fun control(): Boolean {
            return KeyboardInputState.pressed(InputConstants.KEY_LCONTROL) || KeyboardInputState.pressed(InputConstants.KEY_RCONTROL)
        }
    }
}
