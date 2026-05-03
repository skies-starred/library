package xyz.aerii.library.api

import org.lwjgl.glfw.GLFW

val Int.bound: Boolean
    get() = this != -1

val Int.pressed: Boolean
    get() = when {
        !bound -> false
        this > 7 -> keyed
        else -> moused
    }

val Int.keyed: Boolean
    get() {
        val a = GLFW.glfwGetKey(client.window.handle(), this)
        return a == GLFW.GLFW_PRESS || a == GLFW.GLFW_REPEAT
    }

val Int.moused: Boolean
    get() {
        val a = GLFW.glfwGetMouseButton(client.window.handle(), this)
        return a == GLFW.GLFW_PRESS || a == GLFW.GLFW_REPEAT
    }

val shift: Boolean
    get() = GLFW.GLFW_KEY_LEFT_SHIFT.keyed || GLFW.GLFW_KEY_RIGHT_SHIFT.keyed

val ctrl: Boolean
    get() = GLFW.GLFW_KEY_LEFT_CONTROL.keyed || GLFW.GLFW_KEY_RIGHT_CONTROL.keyed