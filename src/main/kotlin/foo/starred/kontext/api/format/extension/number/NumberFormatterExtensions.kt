@file:Suppress("Unused")

package foo.starred.kontext.api.format.extension.number

import foo.starred.kontext.api.format.impl.number.NumberFormatter

fun Number.abbreviate(decimals: Int = 1): String {
    return NumberFormatter.abbreviate(this, decimals)
}

fun String.unabbreviate(): Double {
    return NumberFormatter.unabbreviate(this)
}

fun Number.format(decimals: Boolean): String {
    return NumberFormatter.format(this, decimals)
}

fun Number.suffix(suffix: String, decimals: Int): String {
    return NumberFormatter.suffix(this, suffix, decimals)
}
