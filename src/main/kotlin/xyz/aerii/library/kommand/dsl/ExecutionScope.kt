@file:Suppress("Unused")

package xyz.aerii.library.kommand.dsl

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

class ExecutionScope(private val context: CommandContext<FabricClientCommandSource>) {
    fun string(name: String): String {
        return StringArgumentType.getString(context, name)
    }

    fun bool(name: String): Boolean {
        return BoolArgumentType.getBool(context, name)
    }

    fun int(name: String): Int {
        return IntegerArgumentType.getInteger(context, name)
    }

    fun double(name: String): Double {
        return DoubleArgumentType.getDouble(context, name)
    }

    fun float(name: String): Float {
        return FloatArgumentType.getFloat(context, name)
    }
}