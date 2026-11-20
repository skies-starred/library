@file:Suppress("FunctionName", "UNUSED")

package foo.starred.kontext.api.entity.duck

interface EntityDuck {
    fun `kontext$data`(id: Int): Any?
    fun `kontext$data`(id: Int, value: Any?)
    fun `kontext$data`(): Array<Any?>
    fun `kontext$data`(data: Array<Any?>)
}
