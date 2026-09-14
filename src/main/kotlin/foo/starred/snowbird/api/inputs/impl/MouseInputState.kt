package foo.starred.snowbird.api.inputs.impl

import foo.starred.snowbird.api.inputs.base.IInputState
//~ if >= 26.3 'glfw.GLFW' -> 'sdl.SDLMouse'
import org.lwjgl.sdl.SDLMouse

//? <= 26.2
//import foo.starred.snowbird.api.client

object MouseInputState : IInputState {
    override fun pressed(key: Int): Boolean {
        //? if >= 26.3 {
        return SDLMouse.SDL_GetMouseState(null, null) and (1 shl (key - 1)) != 0
        //? } else {
        /*val a = GLFW.glfwGetMouseButton(client.window.handle(), key)
        return a == GLFW.GLFW_PRESS || a == GLFW.GLFW_REPEAT
        *///? }
    }
}
