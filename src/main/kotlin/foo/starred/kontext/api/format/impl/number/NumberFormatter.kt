package foo.starred.kontext.api.format.impl.number

object NumberFormatter {
    fun abbreviate(number: Number, decimals: Int = 1): String {
        val number = number.toDouble()

        return when {
            number >= 1_000_000_000 -> suffix(number / 1_000_000_000, "B", decimals)
            number >= 1_000_000 -> suffix(number / 1_000_000, "M", decimals)
            number >= 1_000 -> suffix(number / 1_000, "K", decimals)
            else -> if (number % 1.0 == 0.0) number.toInt().toString() else String.format("%.${decimals}f", number)
        }
    }

    fun unabbreviate(string: String): Double {
        val string = string.trim().uppercase()

        val multiplier = when {
            string.endsWith("B") -> 1_000_000_000.0
            string.endsWith("M") -> 1_000_000.0
            string.endsWith("K") -> 1_000.0
            else -> 1.0
        }

        val number = string.dropLastWhile { it in "KMB" }
        return number.toDouble() * multiplier
    }

    fun format(number: Number, decimals: Boolean): String {
        val number = number.toDouble()

        if (number % 1.0 != 0.0 && decimals) {
            return "%,.2f".format(number)
        }

        return "%,d".format(number.toLong())
    }

    fun suffix(number: Number, suffix: String, decimals: Int): String {
        val number = number.toDouble()

        if (number % 1.0 == 0.0 && number < 100) {
            return "${number.toInt()}$suffix"
        }

        return String.format("%.${decimals}f", number).trimEnd('0').trimEnd('.') + suffix
    }
}
