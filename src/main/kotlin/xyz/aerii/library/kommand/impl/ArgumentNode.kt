package xyz.aerii.library.kommand.impl

import com.mojang.brigadier.arguments.ArgumentType
import xyz.aerii.library.kommand.base.ICommandNode

class ArgumentNode<T>(
    name: String,
    val type: ArgumentType<T>
) : ICommandNode(name)