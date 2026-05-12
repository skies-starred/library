@file:Suppress("Unused")

package xyz.aerii.library.kommand.base

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

abstract class ICommandNode(val name: String) {
    val children: MutableList<ICommandNode> = mutableListOf()
    var executor: ((CommandContext<FabricClientCommandSource>) -> Unit)? = null
    var suggests: SuggestionProvider<FabricClientCommandSource>? = null

    fun suggests(block: () -> Collection<String>): ICommandNode {
        suggests = SuggestionProvider { _, builder ->
            for (a in block()) builder.suggest(a)
            builder.buildFuture()
        }

        return this
    }

    fun suggests(provider: SuggestionProvider<FabricClientCommandSource>): ICommandNode {
        suggests = provider
        return this
    }
}