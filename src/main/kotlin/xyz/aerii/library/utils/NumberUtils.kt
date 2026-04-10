@file:Suppress("Unused")

package xyz.aerii.library.utils

@JvmName("timesIntPair")
operator fun Pair<Int, Int>.times(k: Number): Pair<Int, Int> =
    (first * k.toDouble()).toInt() to (second * k.toDouble()).toInt()

@JvmName("timesFloatPair")
operator fun Pair<Float, Float>.times(k: Number): Pair<Float, Float> =
    first * k.toFloat() to second * k.toFloat()

@JvmName("timesDoublePair")
operator fun Pair<Double, Double>.times(k: Number): Pair<Double, Double> =
    first * k.toDouble() to second * k.toDouble()

fun Number.abbreviate(decimals: Int = 1): String {
    val value = this.toDouble()
    return when {
        value >= 1_000_000_000 -> (value / 1_000_000_000).formatWithSuffix("B", decimals)
        value >= 1_000_000 -> (value / 1_000_000).formatWithSuffix("M", decimals)
        value >= 1_000 -> (value / 1_000).formatWithSuffix("K", decimals)
        else -> if (value % 1.0 == 0.0) value.toInt().toString() else String.format("%.${decimals}f", value)
    }
}
