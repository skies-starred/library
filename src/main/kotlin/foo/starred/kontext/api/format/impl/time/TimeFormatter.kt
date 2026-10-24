package foo.starred.kontext.api.format.impl.time

object TimeFormatter {
    val DURATION_SHORT = Regex("""(\d+(?:\.\d+)?)([dhms])""")

    fun parse(string: String): Double {
        var total = 0.0

        for ((value, unit) in DURATION_SHORT.findAll(string.lowercase()).map { it.destructured }) {
            val value = value.toDouble()

            total += when (unit) {
                "d" -> value * 86400
                "h" -> value * 3600
                "m" -> value * 60
                "s" -> value
                else -> 0.0
            }
        }

        return total
    }

    fun format(number: Number, hours: Boolean = true): String {
        val number = number.toInt()

        if (hours) {
            return "%02d:%02d:%02d".format(number / 3600, (number % 3600) / 60, number % 60)
        }

        return "%02d:%02d".format((number % 3600) / 60, number % 60)
    }

    fun unformat(string: String): Double {
        val parts = string.split(':').map { it.toDouble() }

        return when (parts.size) {
            3 -> parts[0] * 3600 + parts[1] * 60 + parts[2]
            2 -> parts[0] * 60 + parts[1]
            1 -> parts[0]
            else -> 0.0
        }
    }

    fun duration(seconds: Number, short: Boolean = false, decimals: Int = 0): String {
        val seconds = seconds.toDouble()

        val total = seconds.toLong()
        val days = total / 86400
        val hours = (total % 86400) / 3600
        val minutes = (total % 3600) / 60
        val rest = total % 60
        val exact = if (decimals > 0) seconds % 60 else rest.toDouble()

        if (total < 60) {
            return seconds(exact, decimals)
        }

        if (short) {
            return when {
                days > 0 -> "${days}d"
                hours > 0 -> "${hours}h"
                else -> "${minutes}m"
            }
        }

        return buildString {
            if (days > 0) append("${days}d ")
            if (hours > 0) append("${hours}h ")
            if (minutes > 0) append("${minutes}m ")
            if (rest > 0) append("${rest}s")
        }.trimEnd()
    }

    fun seconds(seconds: Number, decimals: Int = 0): String {
        return if (decimals > 0) "${String.format("%.${decimals}f", seconds)}s" else "${seconds.toInt()}s"
    }
}
