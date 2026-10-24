package foo.starred.kontext.api.format.impl.roman

object RomanNumeralFormatter {
    private val list = listOf(1000 to "M", 900 to "CM", 500 to "D", 400 to "CD", 100 to "C", 90 to "XC", 50 to "L", 40 to "XL", 10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I")

    fun get(string: String): Int? {
        val string = string.uppercase()
        var i0 = 0
        var i1 = 0

        for ((value, symbol) in list) {
            while (string.startsWith(symbol, i0)) {
                i1 += value
                i0 += symbol.length
            }
        }

        return i1.takeIf { i0 == string.length && it > 0 }
    }

    fun get(int: Int): String {
        var int = int

        return buildString {
            for ((value, symbol) in list) {
                while (int >= value) {
                    append(symbol)
                    int -= value
                }
            }
        }
    }
}
