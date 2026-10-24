@file:Suppress("FunctionName", "UNUSED")

package foo.starred.kontext.api.item.duck

interface ItemStackDuck {
    fun `kontext$data`(id: Int): Any?
    fun `kontext$data`(id: Int, value: Any?)
    fun `kontext$data`(): Array<Any?>
    fun `kontext$data`(data: Array<Any?>)
}
