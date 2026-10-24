package foo.starred.kontext.api.format.extension.roman

import foo.starred.kontext.api.format.impl.roman.RomanNumeralFormatter

fun String.parseRomanNumeral(): Int? {
    return RomanNumeralFormatter.get(this)
}

fun Int.toRomanNumeral(): String {
    return RomanNumeralFormatter.get(this)
}
