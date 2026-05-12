@file:Suppress("Unused")

package xyz.aerii.library.kommand.dsl

import com.mojang.brigadier.arguments.*
import xyz.aerii.library.kommand.base.ICommandNode
import xyz.aerii.library.kommand.impl.ArgumentNode
import xyz.aerii.library.kommand.impl.LiteralNode

class BuilderScope(private val parent: ICommandNode) {
    fun <T> argument(
        name: String,
        type: ArgumentType<T>,
        block: (ExecutionScope.() -> Unit)? = null
    ): ArgumentNode<T> {
        return ArgumentNode(name, type).apply {
            block?.let { executor = { ExecutionScope(it).block() } }
            parent.children += this
        }
    }

    fun string(
        name: String,
        block: (ExecutionScope.() -> Unit)? = null
    ) = argument(name, StringArgumentType.string(), block)

    fun greedyString(
        name: String,
        block: (ExecutionScope.() -> Unit)? = null
    ) = argument(name, StringArgumentType.greedyString(), block)

    fun word(
        name: String,
        block: (ExecutionScope.() -> Unit)? = null
    ) = argument(name, StringArgumentType.word(), block)

    fun int(
        name: String,
        min: Int = Int.MIN_VALUE,
        max: Int = Int.MAX_VALUE,
        block: (ExecutionScope.() -> Unit)? = null
    ) = argument(name, IntegerArgumentType.integer(min, max), block)

    fun double(
        name: String,
        min: Double = -Double.MAX_VALUE,
        max: Double = Double.MAX_VALUE,
        block: (ExecutionScope.() -> Unit)? = null
    ) = argument(name, DoubleArgumentType.doubleArg(min, max), block)

    fun float(
        name: String,
        min: Float = -Float.MAX_VALUE,
        max: Float = Float.MAX_VALUE,
        block: (ExecutionScope.() -> Unit)? = null
    ) = argument(name, FloatArgumentType.floatArg(min, max), block)

    fun bool(
        name: String,
        block: (ExecutionScope.() -> Unit)? = null
    ) = argument(name, BoolArgumentType.bool(), block)

    fun executes(block: ExecutionScope.() -> Unit) {
        parent.executor = { ExecutionScope(it).block() }
    }

    operator fun ICommandNode.div(other: ICommandNode): ICommandNode {
        parent.children -= other
        children += other
        return other
    }

    operator fun ICommandNode.div(other: String): LiteralNode {
        return LiteralNode(other).also(children::add)
    }

    operator fun String.div(other: ICommandNode): ICommandNode {
        parent.children -= other

        literal(this).apply {
            children += other
        }

        return other
    }

    operator fun String.div(other: String): LiteralNode {
        return LiteralNode(this).apply {
            children += LiteralNode(other)
            parent.children += this
        }.children.last() as LiteralNode
    }

    operator fun String.invoke(block: ExecutionScope.() -> Unit): LiteralNode {
        return literal(this).apply {
            executor = { ExecutionScope(it).block() }
        }
    }

    private fun literal(name: String): LiteralNode {
        return LiteralNode(name).also(parent.children::add)
    }
}