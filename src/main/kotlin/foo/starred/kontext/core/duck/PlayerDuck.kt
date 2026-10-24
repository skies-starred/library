@file:Suppress("FunctionName")

package foo.starred.kontext.core.duck

import net.minecraft.network.chat.Component

interface PlayerDuck {
    fun `kontext$name$emblem$int`(): Int
    fun `kontext$name$emblem$tag`(tag: Component): Component

    fun `kontext$size`(): Int
    fun `kontext$size$x`(): Float
    fun `kontext$size$y`(): Float
    fun `kontext$size$z`(): Float
    fun `kontext$size`(i0: Float, i1: Float, i2: Float)
}
