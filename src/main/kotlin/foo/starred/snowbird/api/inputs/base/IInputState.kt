package foo.starred.snowbird.api.inputs.base

import com.mojang.blaze3d.platform.InputConstants

interface IInputState {
    fun pressed(key: InputConstants.Key): Boolean
}
