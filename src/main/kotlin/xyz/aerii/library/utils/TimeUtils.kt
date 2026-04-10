@file:Suppress("Unused")

package xyz.aerii.library.utils

fun Number.toDuration(short: Boolean = false, secondsDecimals: Int = 0, secondsOnly: Boolean = false): String {
    val totalSeconds = this.toDouble()

    if (secondsOnly) {
        val secs = if (secondsDecimals > 0) totalSeconds else totalSeconds.toLong().toDouble()
        return secs.formatSeconds(secondsDecimals)
    }

    val s = totalSeconds.toLong()
    val d = s / 86400
    val h = (s % 86400) / 3600
    val m = (s % 3600) / 60
    val rs = s % 60
    val fs = if (secondsDecimals > 0) totalSeconds % 60 else rs.toDouble()

    if (short) {
        return when {
            d > 0 -> "${d}d"
            h > 0 -> "${h}h"
            m > 0 -> "${m}m"
            else -> fs.formatSeconds(secondsDecimals)
        }
    }

    return buildString {
        if (d > 0) append("${d}d ")
        if (h > 0) append("${h}h ")
        if (m > 0) append("${m}m ")
        if (d > 0 || h > 0 || m > 0) {
            if (rs > 0) append("${rs}s")
        } else {
            append(fs.formatSeconds(secondsDecimals))
        }
    }.trimEnd()
}

fun Number.toDurationFromMillis(short: Boolean = false, secondsDecimals: Int = 0, secondsOnly: Boolean = false): String =
    (this.toDouble() / 1000).toDuration(short, secondsDecimals, secondsOnly)

fun String.fromDuration(): Double {
    var total = 0.0

    for ((value, unit) in DURATION_REGEX.findAll(lowercase()).map { it.destructured }) {
        val v = value.toDouble()
        total += when (unit) {
            "d" -> v * 86400
            "h" -> v * 3600
            "m" -> v * 60
            "s" -> v
            else -> 0.0
        }
    }

    return total
}

fun String.fromLongDuration(): Double {
    var total = 0.0

    for ((value, unit) in LONG_DURATION_REGEX.findAll(lowercase()).map { it.destructured }) {
        val v = value.toDouble()
        total += when (unit) {
            "day", "days" -> v * 86400
            "hour", "hours" -> v * 3600
            "minute", "minutes" -> v * 60
            "second", "seconds" -> v
            else -> 0.0
        }
    }

    return total
}

fun Number.toHMS(): String {
    val t = toInt()
    return "%02d:%02d:%02d".format(t / 3600, (t % 3600) / 60, t % 60)
}

fun String.fromHMS(): Double {
    val p = split(':').map { it.toDouble() }
    return when (p.size) {
        3 -> p[0] * 3600 + p[1] * 60 + p[2]
        2 -> p[0] * 60 + p[1]
        1 -> p[0]
        else -> 0.0
    }
}

fun Number.toMS(): String {
    val t = toInt()
    return "%02d:%02d".format((t % 3600) / 60, t % 60)
}

fun Number.formatted(decimal: Boolean = true): String {
    val value = toDouble()

    if (value % 1.0 != 0.0 && decimal) return "%,.2f".format(value)
    return "%,d".format(value.toLong())
}

fun Number.formatWithSuffix(suffix: String, decimals: Int): String {
    val v = toDouble()

    return if (v % 1.0 == 0.0 && v < 100) "${toInt()}$suffix"
    else String.format("%.${decimals}f", this).trimEnd('0').trimEnd('.') + suffix
}

fun Number.formatSeconds(decimals: Int): String =
    if (decimals > 0) "${String.format("%.${decimals}f", this)}s" else "${toInt()}s"