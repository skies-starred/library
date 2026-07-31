@file:Suppress("Unused")

package foo.starred.snowbird.kommand

import foo.starred.snowbird.kommand.dsl.BuilderScope
import foo.starred.snowbird.kommand.impl.LiteralNode
import foo.starred.snowbird.kommand.loader.CommandLoader

interface ICommand {
    fun command(name: String, block: BuilderScope.() -> Unit) {
        val root = LiteralNode(name)
        BuilderScope(root).block()
        CommandLoader.roots += root
    }
}