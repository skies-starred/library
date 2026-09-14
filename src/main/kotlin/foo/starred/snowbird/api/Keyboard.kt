package foo.starred.snowbird.api

import foo.starred.snowbird.api.inputs.impl.GenericInputState
import foo.starred.snowbird.api.inputs.impl.KeyboardInputState
import foo.starred.snowbird.api.inputs.impl.MouseInputState

@Deprecated("Use GenericInputState.bound()")
val Int.bound: Boolean
    get() = GenericInputState.bound(this)

@Deprecated("Use GenericInputState.pressed()")
val Int.pressed: Boolean
    get() {
        return when {
            !GenericInputState.bound(this) -> false
            this > 7 -> KeyboardInputState.pressed(KeyboardInputState.vanilla(this))
            else -> MouseInputState.pressed(MouseInputState.vanilla(this))
        }
    }

@Deprecated("Use KeyboardInputState.pressed()")
val Int.keyed: Boolean
    get() = KeyboardInputState.pressed(KeyboardInputState.vanilla(this))

@Deprecated("Use MouseInputState.pressed()")
val Int.moused: Boolean
    get() = MouseInputState.pressed(MouseInputState.vanilla(this))

@Deprecated("Use GenericInputState.States.shift()")
val shift: Boolean
    get() = KeyboardInputState.States.shift()

@Deprecated("Use GenericInputState.States.control()")
val ctrl: Boolean
    get() = KeyboardInputState.States.control()
