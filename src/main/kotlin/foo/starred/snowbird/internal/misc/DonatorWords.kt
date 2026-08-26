package foo.starred.snowbird.internal.misc

import com.google.gson.JsonObject
import foo.starred.kommand.IKommand
import foo.starred.kommand.scopes.KommandCommandScope
import foo.starred.snowbird.Snowbird
import foo.starred.snowbird.api.EMPTY_COMPONENT
import foo.starred.snowbird.api.lie
import foo.starred.snowbird.api.name
import foo.starred.snowbird.handlers.minecraft.AbstractWords
import foo.starred.snowbird.handlers.parser.parse
import foo.starred.snowbird.internal.utils.mod
import foo.starred.snowbird.internal.web.WebUtils.request
import foo.starred.snowbird.utils.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.util.FormattedCharSequence

object DonatorWords : AbstractWords(), IKommand<FabricClientCommandSource> {
    override val loader: KommandCommandScope<FabricClientCommandSource> = Snowbird.COMMANDS

    private const val SKIP = "aerii_ds_bypass"

    private val map = mutableMapOf<String, Component>()
    private var bool: Boolean = false

    init {
        skips = SKIP

        "https://data.starred.foo/donor/names".request {
            onSuccess<JsonObject> { json ->
                map.clear()

                for ((k, v) in json.entrySet()) {
                    val a = v.asString.parse()
                    map[k] = a
                    put(k, a.string, a, a.visualOrderText)
                }

                build()
            }
        }

        command("snowbird") {
            "name" / "toggle" {
                val a = name
                val b = map[a] ?: return@invoke "<red>You don't have a custom name!".mod()
                val c = map1.contains(a)

                if (c) remove(a) else put(a, b.string, b, b.visualOrderText)
                build()
                "${if (c) "<red>Disabled" else "<green>Enabled"}<r> custom name! Run this command again to ${if (c) "<green>enable" else "<red>disable"}<r> it.".mod()
            }

            "name" / "toggle" / "all" {
                val a = map.keys.any(map1::contains)

                if (!bool && a) {
                    "Are you sure you want to <red>disable ALL donator names?<r> Run this command again to confirm :(".mod()
                    bool = true
                    return@invoke
                }

                for ((k, v) in map) {
                    if (a) remove(k)
                    else put(k, v.string, v, v.visualOrderText)
                }

                build()
                bool = false
                "${if (a) "<red>Disabled" else "<green>Enabled"}<r> all donator names. Run this command again to ${if (a) "<green>enable" else "<red>disable"}<r> them.".mod()
            }

            "name" / "list" {
                "Donator names:".mod()
                for ((a, b) in map2) " <dark_gray>• <r>$a <gray>-> ".parse().skip().append(b.toComponent()).lie()
            }

            "name" / "list" / string("search") {
                val s = string("search")
                var i = 0

                "Donator names containing <green>\"$s\"<r>:".mod { skip() }
                for ((a, b) in map2) {
                    val c = b.toComponent()
                    if (!a.contains(s, true) && !c.string.contains(s, true)) continue

                    i++
                    " <dark_gray>• <r>$a <gray>-> ".parse().skip().append(c).lie()
                }

                if (i != 0) return@string
                "Couldn't find any names containing <green>\"$s\"<r>!".mod { skip() }
            }
        }
    }

    private fun MutableComponent.skip(): MutableComponent {
        return copy().withStyle(style.withInsertion(SKIP))
    }

    private fun FormattedCharSequence.toComponent(): Component {
        val builder = EMPTY_COMPONENT.copy()

        accept { _, style, cp ->
            builder.append(Character.toString(cp).literal().withStyle(style))
            true
        }

        return builder
    }
}
