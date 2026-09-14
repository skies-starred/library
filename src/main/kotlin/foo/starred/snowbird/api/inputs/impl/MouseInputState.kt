package foo.starred.snowbird.api.inputs.impl

import com.mojang.blaze3d.platform.InputConstants
import foo.starred.snowbird.api.client
import foo.starred.snowbird.api.inputs.base.IInputState
//~ if >= 26.3 'glfw.GLFW' -> 'sdl.SDLMouse'
import org.lwjgl.sdl.SDLMouse

//? <= 26.2
//import foo.starred.snowbird.api.client

object MouseInputState : IInputState {
    override fun pressed(key: InputConstants.Key): Boolean {
        //? if >= 26.3 {
        return SDLMouse.SDL_GetMouseState(null, null) and (1 shl (key.value - 1)) != 0
        //? } else {
        /*val a = GLFW.glfwGetMouseButton(client.window.handle(), key.value)
        return a == GLFW.GLFW_PRESS || a == GLFW.GLFW_REPEAT
        *///? }
    }

    fun pressed(int: Int): Boolean {
        //? if >= 26.3 {
        return SDLMouse.SDL_GetMouseState(null, null) and (1 shl (int - 1)) != 0
        //? } else {
        /*val a = GLFW.glfwGetMouseButton(client.window.handle(), int)
        return a == GLFW.GLFW_PRESS || a == GLFW.GLFW_REPEAT
        *///? }
    }

    object Position {
        object Raw {
            val x: Float
                get() = client.mouseHandler.xpos().toFloat()

            val y: Float
                get() = client.mouseHandler.ypos().toFloat()
        }

        object Scaled {
            val x: Float
                get() = client.mouseHandler.getScaledXPos(client.window).toFloat()

            val y: Float
                get() = client.mouseHandler.getScaledYPos(client.window).toFloat()
        }
    }
}
