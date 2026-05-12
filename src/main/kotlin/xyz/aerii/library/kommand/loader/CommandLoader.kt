package xyz.aerii.library.kommand.loader

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.ClientCommands
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import xyz.aerii.library.kommand.impl.ArgumentNode
import xyz.aerii.library.kommand.impl.LiteralNode

object CommandLoader {
    val roots = mutableListOf<LiteralNode>()

    init {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            for (r in roots) dispatcher.register(literal(r))
        }
    }

    private fun literal(node: LiteralNode): LiteralArgumentBuilder<FabricClientCommandSource> {
        val builder = ClientCommands.literal(node.name)

        node.executor?.let { a ->
            builder.executes { b ->
                a(b)
                Command.SINGLE_SUCCESS
            }
        }

        for (c in node.children) {
            when (c) {
                is LiteralNode -> builder.then(literal(c))
                is ArgumentNode<*> -> builder.then(argument(c))
            }
        }

        return builder
    }

    private fun <T> argument(node: ArgumentNode<T>): RequiredArgumentBuilder<FabricClientCommandSource, T> {
        val builder = RequiredArgumentBuilder.argument<FabricClientCommandSource, T>(node.name, node.type)

        node.suggests?.let {
            builder.suggests(it)
        }

        node.executor?.let { a ->
            builder.executes { b ->
                a(b)
                Command.SINGLE_SUCCESS
            }
        }

        for (c in node.children) {
            when (c) {
                is LiteralNode -> builder.then(literal(c))
                is ArgumentNode<*> -> builder.then(argument(c))
            }
        }

        return builder
    }
}