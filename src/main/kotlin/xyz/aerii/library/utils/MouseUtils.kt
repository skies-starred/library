@file:Suppress("Unused")

package xyz.aerii.library.utils

import xyz.aerii.library.api.client
import kotlin.math.max

val mouseRX: Float
    get() = client.mouseHandler.xpos().toFloat()

val mouseRY: Float
    get() = client.mouseHandler.ypos().toFloat()

val mouseSX: Float
    get() = mouseRX * client.window.guiScaledWidth / max(1, client.window.width)

val mouseSY: Float
    get() = mouseRY * client.window.guiScaledHeight / max(1, client.window.height)

@JvmOverloads
fun hovered(x: Number, y: Number, w: Number, h: Number, scaled: Boolean = false): Boolean {
    val x = x.toFloat()
    val y = y.toFloat()
    val w = w.toFloat()
    val h = h.toFloat()

    return if (scaled) mouseSX in x..(x + w) && mouseSY in y..(y + h)
    else mouseRX in x..(x + w) && mouseRY in y..(y + h)
}

@JvmOverloads
fun hovered(x: Number, y: Number, w: Number, scaled: Boolean = false): Boolean {
    val x = x.toFloat()
    val y = y.toFloat()
    val w = w.toFloat()

    return if (scaled) mouseSX in x..(x + w) && mouseSY >= y
    else mouseRX in x..(x + w) && mouseRY >= y
}