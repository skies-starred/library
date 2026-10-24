package foo.starred.kontext.abstraction.chat.color

enum class MinecraftColors(color: Number, val char: Char, val rgb: Int = color.toInt() and 0xFFFFFF) {
    BLACK(0xFF000000, '0'),
    DARK_BLUE(0xFF0000AA, '1'),
    DARK_GREEN(0xFF00AA00, '2'),
    DARK_AQUA(0xFF00AAAA, '3'),
    DARK_RED(0xFFAA0000, '4'),
    DARK_PURPLE(0xFFAA00AA, '5'),
    GOLD(0xFFFFAA00, '6'),
    GRAY(0xFFAAAAAA, '7'),
    DARK_GRAY(0xFF555555, '8'),
    BLUE(0xFF5555FF, '9'),
    GREEN(0xFF55FF55, 'a'),
    AQUA(0xFF55FFFF, 'b'),
    RED(0xFFFF5555, 'c'),
    PINK(0xFFFF55FF, 'd'),
    YELLOW(0xFFFFFF55, 'e'),
    WHITE(0xFFFFFFFF, 'f');

    val color: Int = color.toInt()

    companion object {
        fun get(char: Char): MinecraftColors? {
            return entries.find { it.char == char }
        }

        fun get(color: Number): MinecraftColors? {
            val rgb = color.toInt() and 0xFFFFFF
            return entries.find { it.rgb == rgb }
        }

        fun get(string: String): MinecraftColors? {
            return entries.find { it.name.equals(string, ignoreCase = true) }
        }
    }
}
