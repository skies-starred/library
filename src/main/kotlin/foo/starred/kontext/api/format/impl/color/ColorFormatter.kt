@file:Suppress("NOTHING_TO_INLINE")

package foo.starred.kontext.api.format.impl.color

object ColorFormatter {
    fun rgba(int: Int): Int {
        return (int shl 8) or (int ushr 24)
    }

    fun rgba(r: Int, g: Int, b: Int, a: Int = 255): Int {
        return (r shl 24) or (g shl 16) or (b shl 8) or a
    }

    fun argb(int: Int): Int {
        return (int ushr 8) or (int shl 24)
    }

    fun argb(r: Int, g: Int, b: Int, a: Int = 255): Int {
        return (a shl 24) or (r shl 16) or (g shl 8) or b
    }

    fun alpha(int: Int, alpha: Float, rgba: Boolean = false): Int {
        val alpha = (alpha * 255f).toInt().coerceIn(0, 255)

        if (rgba) {
            return (int and 0xFFFFFF00.toInt()) or alpha
        }

        return (int and 0x00FFFFFF) or (alpha shl 24)
    }

    fun brighten(int: Int, factor: Float, rgba: Boolean = false): Int {
        val r = (red(int) * factor).toInt().coerceAtMost(255)
        val g = (green(int) * factor).toInt().coerceAtMost(255)
        val b = (blue(int) * factor).toInt().coerceAtMost(255)

        if (rgba) {
            return rgba(r, g, b, alpha(int))
        }

        return argb(r, g, b, alpha(int))
    }

    inline fun red(int: Int): Int {
        return int shr 16 and 0xFF
    }

    inline fun green(int: Int): Int {
        return int shr 8 and 0xFF
    }

    inline fun blue(int: Int): Int {
        return int and 0xFF
    }

    inline fun alpha(int: Int): Int {
        return int shr 24 and 0xFF
    }
}
