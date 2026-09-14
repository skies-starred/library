package foo.starred.snowbird.api

import foo.starred.snowbird.api.inputs.impl.GenericInputState
import foo.starred.snowbird.api.inputs.impl.KeyboardInputState
import foo.starred.snowbird.api.inputs.impl.MouseInputState

@Deprecated("Use GenericInputState.bound()")
val Int.bound: Boolean
    get() = GenericInputState.bound(this)

@Deprecated("Use GenericInputState.pressed()")
val Int.pressed: Boolean
    get() = GenericInputState.pressed(this)

@Deprecated("Use KeyboardInputState.pressed()")
val Int.keyed: Boolean
    get() = KeyboardInputState.pressed(this)

@Deprecated("Use MouseInputState.pressed()")
val Int.moused: Boolean
    get() = MouseInputState.pressed(this)

@Deprecated("Use GenericInputState.States.shift()")
val shift: Boolean
    get() = GenericInputState.States.shift()

@Deprecated("Use GenericInputState.States.control()")
val ctrl: Boolean
    get() = GenericInputState.States.control()
