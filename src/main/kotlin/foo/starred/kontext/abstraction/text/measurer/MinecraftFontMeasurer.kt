@file:Suppress("UNUSED")

package foo.starred.kontext.abstraction.text.measurer

import foo.starred.kontext.abstraction.text.generic.MinecraftFont
import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence

object MinecraftFontMeasurer {
    val height by lazy {
        MinecraftFont.instanceNullable?.lineHeight ?: 9
    }

    fun constrain(string: String): Pair<Int, Int> {
        return width(string) to height
    }

    fun constrain(component: Component): Pair<Int, Int> {
        return width(component) to height
    }

    fun constrain(formatted: FormattedCharSequence): Pair<Int, Int> {
        return width(formatted) to height
    }

    @JvmName("constrain_string_multi")
    fun constrain(string: Collection<String>, spacing: Int = 2): Pair<Int, Int> {
        val width = width(string).maxOrNull() ?: 0
        val height = if (string.isEmpty()) 0 else string.size * height + (string.size - 1) * spacing

        return width to height
    }

    @JvmName("constrain_component_multi")
    fun constrain(component: Collection<Component>, spacing: Int = 2): Pair<Int, Int> {
        val width = width(component).maxOrNull() ?: 0
        val height = if (component.isEmpty()) 0 else component.size * height + (component.size - 1) * spacing

        return width to height
    }

    @JvmName("constrain_fcs_multi")
    fun constrain(formatted: Collection<FormattedCharSequence>, spacing: Int = 2): Pair<Int, Int> {
        val width = width(formatted).maxOrNull() ?: 0
        val height = if (formatted.isEmpty()) 0 else formatted.size * height + (formatted.size - 1) * spacing

        return width to height
    }

    fun width(string: String): Int {
        return MinecraftFont.instanceNullable?.width(string) ?: 0
    }

    fun width(component: Component): Int {
        return MinecraftFont.instanceNullable?.width(component) ?: 0
    }

    fun width(formatted: FormattedCharSequence): Int {
        return MinecraftFont.instanceNullable?.width(formatted) ?: 0
    }

    @JvmName("width_string_multi")
    fun width(string: Collection<String>): List<Int> {
        val font = MinecraftFont.instanceNullable ?: return List(string.size) { 0 }
        return string.map { font.width(it) }
    }

    @JvmName("width_component_multi")
    fun width(component: Collection<Component>): List<Int> {
        val font = MinecraftFont.instanceNullable ?: return List(component.size) { 0 }
        return component.map { font.width(it) }
    }

    @JvmName("width_fcs_multi")
    fun width(formatted: Collection<FormattedCharSequence>): List<Int> {
        val font = MinecraftFont.instanceNullable ?: return List(formatted.size) { 0 }
        return formatted.map { font.width(it) }
    }
}
