@file:Suppress("Unused")

package xyz.aerii.library.utils

import java.awt.Color

val Int.red: Int
    get() = this shr 16 and 0xFF

val Int.green: Int
    get() = this shr 8 and 0xFF

val Int.blue: Int
    get() = this and 0xFF

val Int.alpha: Int
    get() = this shr 24 and 0xFF

fun Int.rgba(): Int =
    (this shl 8) or (this ushr 24)

fun Int.argb(): Int =
    (this ushr 8) or (this shl 24)

@JvmOverloads
fun argb(r: Int, g: Int, b: Int, a: Int = 255): Int =
    (a shl 24) or (r shl 16) or (g shl 8) or b

@JvmOverloads
fun rgba(r: Int, g: Int, b: Int, a: Int = 255): Int =
    (r shl 24) or (g shl 16) or (b shl 8) or a

@JvmOverloads
fun Int.withAlpha(alpha: Float, rgba: Boolean = false): Int {
    val a = (alpha * 255f).toInt().coerceIn(0, 255)
    return if (rgba) (this and 0xFFFFFF00.toInt()) or a else (this and 0x00FFFFFF) or (a shl 24)
}

fun Color.brighten(factor: Float): Color {
    return Color(
        (red * factor).toInt().coerceAtMost(255),
        (green * factor).toInt().coerceAtMost(255),
        (blue * factor).toInt().coerceAtMost(255),
        alpha
    )
}

fun Int.brighten(factor: Float): Int {
    val r = (red * factor).toInt().coerceAtMost(255)
    val g = (green * factor).toInt().coerceAtMost(255)
    val b = (blue * factor).toInt().coerceAtMost(255)

    return argb(r, g, b, alpha)
}

val colors = mapOf(
    "black" to 0x000000,
    "dark_blue" to 0x0000AA,
    "dark_green" to 0x00AA00,
    "dark_aqua" to 0x00AAAA,
    "dark_red" to 0xAA0000,
    "dark_purple" to 0xAA00AA,
    "gold" to 0xFFAA00,
    "orange" to 0xFFAA00,
    "gray" to 0xAAAAAA,
    "dark_gray" to 0x555555,
    "blue" to 0x5555FF,
    "green" to 0x55FF55,
    "aqua" to 0x55FFFF,
    "red" to 0xFF5555,
    "pink" to 0xFF55FF,
    "yellow" to 0xFFFF55,
    "white" to  0xFFFFFF
)