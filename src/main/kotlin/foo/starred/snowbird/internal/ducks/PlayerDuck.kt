@file:Suppress("FunctionName")

package foo.starred.snowbird.internal.ducks

import net.minecraft.network.chat.Component

interface PlayerDuck {
    fun `snowbird$name$emblem$int`(): Int
    fun `snowbird$name$emblem$tag`(tag: Component): Component

    fun `snowbird$size`(): Int
    fun `snowbird$size$x`(): Float
    fun `snowbird$size$y`(): Float
    fun `snowbird$size$z`(): Float
    fun `snowbird$size`(i0: Float, i1: Float, i2: Float)
}
