package foo.starred.kontext.abstraction.input.impl

import com.mojang.blaze3d.platform.InputConstants
import foo.starred.kontext.abstraction.input.base.IInputState

//? <= 26.2
//import foo.starred.kontext.abstraction.client.extension.client

object KeyboardInputState : IInputState {
    override fun pressed(key: InputConstants.Key): Boolean {
        //~ if >= 26.3 'isKeyDown(client.window, key.value)' -> 'isKeyDown(key.value)'
        return InputConstants.isKeyDown(key.value)
    }

    fun vanilla(key: Int): InputConstants.Key {
        //~ if >= 26.3 'KEYSYM' -> 'KEYBOARD'
        return InputConstants.Type.KEYBOARD.getOrCreate(key)
    }

    object States {
        fun shift(): Boolean {
            return pressed(vanilla(InputConstants.KEY_LSHIFT)) || pressed(vanilla(InputConstants.KEY_RSHIFT))
        }

        fun control(): Boolean {
            return pressed(vanilla(InputConstants.KEY_LCONTROL)) || pressed(vanilla(InputConstants.KEY_RCONTROL))
        }
    }
}
