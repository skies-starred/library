@file:Suppress("Unused")

package foo.starred.kontext.api.format.extension.time

import foo.starred.kontext.api.format.impl.time.TimeFormatter

fun String.parseTime(): Double {
    return TimeFormatter.parse(this)
}

fun Number.parseDuration(short: Boolean = false, decimals: Int = 0): String {
    return TimeFormatter.duration(this, short, decimals)
}

fun Number.formatTime(hours: Boolean = true): String {
    return TimeFormatter.format(this, hours)
}

fun String.unformatTime(): Double {
    return TimeFormatter.unformat(this)
}
