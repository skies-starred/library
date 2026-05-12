@file:Suppress("Unused")

package xyz.aerii.library.kommand

import xyz.aerii.library.kommand.dsl.BuilderScope
import xyz.aerii.library.kommand.impl.LiteralNode
import xyz.aerii.library.kommand.loader.CommandLoader

interface ICommand {
    fun command(name: String, block: BuilderScope.() -> Unit) {
        val root = LiteralNode(name)
        BuilderScope(root).block()
        CommandLoader.roots += root
    }
}