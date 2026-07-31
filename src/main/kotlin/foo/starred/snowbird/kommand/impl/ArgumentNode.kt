package foo.starred.snowbird.kommand.impl

import com.mojang.brigadier.arguments.ArgumentType
import foo.starred.snowbird.kommand.base.ICommandNode

class ArgumentNode<T>(
    name: String,
    val type: ArgumentType<T>
) : ICommandNode(name)