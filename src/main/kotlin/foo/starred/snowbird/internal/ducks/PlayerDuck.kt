@file:Suppress("FunctionName")

package foo.starred.snowbird.internal.ducks

interface PlayerDuck {
    fun `snowbird$size`(): Int
    fun `snowbird$size$x`(): Float
    fun `snowbird$size$y`(): Float
    fun `snowbird$size$z`(): Float
    fun `snowbird$size`(i0: Float, i1: Float, i2: Float)
}