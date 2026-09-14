package foo.starred.snowbird.api.inputs.impl

import com.mojang.blaze3d.platform.InputConstants
import foo.starred.snowbird.api.inputs.base.IInputState

object GenericInputState : IInputState {
    override fun pressed(key: InputConstants.Key): Boolean {
        return bound(key.value) && when (key.type) {
            //~ if >= 26.3 'KEYSYM' -> 'KEYBOARD'
            InputConstants.Type.KEYBOARD -> KeyboardInputState.pressed(key)
            InputConstants.Type.MOUSE -> MouseInputState.pressed(key)
            //? if <= 26.2
            //else -> false
        }
    }

    fun name(key: InputConstants.Key): String {
        return when (key.type) {
            //~ if >= 26.3 'KEYSYM' -> 'KEYBOARD'
            InputConstants.Type.KEYBOARD -> key.displayName.string
            InputConstants.Type.MOUSE -> "Mouse ${key.value}"
            //? if <= 26.2
            //else -> "Unknown"
        }
    }

    fun bound(key: Int): Boolean {
        return key != InputConstants.UNKNOWN.value
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
