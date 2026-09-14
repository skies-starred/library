@file:Suppress("Unused")

package foo.starred.snowbird.utils

import foo.starred.snowbird.api.inputs.impl.MouseInputState

@Deprecated("Use MouseInputState.Position")
val mouseRX: Float
    get() = MouseInputState.Position.Raw.x

@Deprecated("Use MouseInputState.Position")
val mouseRY: Float
    get() = MouseInputState.Position.Raw.y

@Deprecated("Use MouseInputState.Position")
val mouseSX: Float
    get() = MouseInputState.Position.Scaled.x

@Deprecated("Use MouseInputState.Position")
val mouseSY: Float
    get() = MouseInputState.Position.Scaled.y

@Deprecated("Deprecated")
fun hovered(x: Number, y: Number, w: Number, h: Number, scaled: Boolean = false): Boolean {
    val x = x.toFloat()
    val y = y.toFloat()
    val w = w.toFloat()
    val h = h.toFloat()

    return if (scaled) mouseSX in x..(x + w) && mouseSY in y..(y + h)
    else mouseRX in x..(x + w) && mouseRY in y..(y + h)
}
