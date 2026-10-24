package foo.starred.kontext.api.format.extension.color

import foo.starred.kontext.api.format.impl.color.ColorFormatter

inline val Int.red: Int
    get() = ColorFormatter.red(this)

inline val Int.green: Int
    get() = ColorFormatter.green(this)

inline val Int.blue: Int
    get() = ColorFormatter.blue(this)

inline val Int.alpha: Int
    get() = ColorFormatter.alpha(this)

fun Int.rgba(): Int {
    return ColorFormatter.rgba(this)
}

fun Int.argb(): Int {
    return ColorFormatter.argb(this)
}

fun Int.alpha(alpha: Float, rgba: Boolean = false): Int {
    return ColorFormatter.alpha(this, alpha, rgba)
}

fun Int.brighten(factor: Float, rgba: Boolean = false): Int {
    return ColorFormatter.brighten(this, factor, rgba)
}
