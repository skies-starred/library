@file:Suppress("UNUSED")

package foo.starred.kontext.api.format.extension.string

import foo.starred.kontext.api.format.impl.string.StringFormatter
import net.minecraft.network.chat.Component

fun String.stripped(): String {
    return StringFormatter.strip(this)
}

fun Component.stripped(): String {
    return StringFormatter.strip(this)
}

fun String.camelCased(): String {
    return StringFormatter.camelCased(this)
}

fun String.contains(vararg other: String, ignoreCase: Boolean = false): Boolean {
    return other.any { contains(it, ignoreCase) }
}
