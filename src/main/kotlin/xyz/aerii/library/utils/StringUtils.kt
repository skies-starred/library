@file:Suppress("Unused")

package xyz.aerii.library.utils

import net.minecraft.network.chat.Component
//~ if >= 1.21.11 'Util' -> 'util.Util'
import net.minecraft.util.Util

val DURATION_REGEX = Regex("""(\d+(?:\.\d+)?)([dhms])""")
val LONG_DURATION_REGEX = Regex("""(\d+(?:\.\d+)?)\s+(day|days|hour|hours|minute|minutes|second|seconds)""")
val STRIP_COLOR_REGEX = Regex("(?i)§.")

fun String.stripped(): String {
    return STRIP_COLOR_REGEX.replace(this, "")
}

fun Component.stripped(): String {
    return STRIP_COLOR_REGEX.replace(string, "")
}

fun String.toCamelCase(): String {
    return this
        .split(" ", "_", "-")
        .filter { it.isNotBlank() }
        .mapIndexed { index, word ->
            val lower = word.lowercase()
            if (index == 0) lower else lower.replaceFirstChar { it.uppercase() }
        }
        .joinToString("")
}

fun String.unabbreviate(): Double {
    val s = trim().uppercase()

    val multiplier = when {
        s.endsWith("B") -> 1_000_000_000.0
        s.endsWith("M") -> 1_000_000.0
        s.endsWith("K") -> 1_000.0
        else -> 1.0
    }

    val number = s.dropLastWhile { it in "KMB" }
    return number.toDouble() * multiplier
}

fun Int.plural(w1: String, w2: String): String {
    return if (equals(1)) w1 else w2
}

fun String.open() {
    Util.getPlatform().openUri(this)
}