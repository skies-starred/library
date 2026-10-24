package foo.starred.kontext.api.format.impl.string

import net.minecraft.network.chat.Component

object StringFormatter {
    val STRIP_COLOR = Regex("(?i)§.")

    fun strip(string: String): String {
        return STRIP_COLOR.replace(string, "")
    }

    fun strip(component: Component): String {
        return STRIP_COLOR.replace(component.string, "")
    }

    fun camelCased(string: String): String {
        return string
            .split(" ", "_", "-")
            .filter { it.isNotBlank() }
            .mapIndexed { index, word ->
                val lower = word.lowercase()
                if (index == 0) lower else lower.replaceFirstChar { it.uppercase() }
            }
            .joinToString("")
    }

    fun titleCased(string: String): String {
        return string
            .split(" ", "_", "-")
            .filter { it.isNotBlank() }
            .joinToString(" ") { a -> a.replaceFirstChar { b -> b.uppercase() } }
    }
}
