@file:Suppress("FunctionName")

package xyz.aerii.library.internal.ducks

interface PlayerDuck {
    fun `aerii$library$size`(): Int
    fun `aerii$library$size$x`(): Float
    fun `aerii$library$size$y`(): Float
    fun `aerii$library$size$z`(): Float
    fun `aerii$library$size`(i0: Float, i1: Float, i2: Float)
}