package xyz.aerii.library.internal.misc

import com.google.gson.JsonObject
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.util.FormattedCharSequence
import xyz.aerii.library.api.EMPTY_COMPONENT
import xyz.aerii.library.api.lie
import xyz.aerii.library.api.name
import xyz.aerii.library.handlers.minecraft.AbstractWords
import xyz.aerii.library.handlers.parser.parse
import xyz.aerii.library.internal.web.WebUtils.request
import xyz.aerii.library.kommand.ICommand
import xyz.aerii.library.utils.literal
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

object DonatorWords : AbstractWords(), ICommand {
    private const val SKIP = "aerii_ds_bypass"

    private val map = mutableMapOf<String, Component>()
    private var bool: Boolean = false

    init {
        skips = SKIP

        "https://data.aerii.xyz/donators.json".request {
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

        command("aerii") {
            "name" / "toggle" {
                val a = name
                val b = map[a] ?: return@invoke "<#CBA6F7>[Aerii] <red>You don't have a custom name!".parse().lie()
                val c = map1.contains(a)

                if (c) remove(a) else put(a, b.string, b, b.visualOrderText)
                build()
                "<#CBA6F7>[Aerii] ${if (c) "<red>Disabled" else "<green>Enabled"}<r> custom name! Run this command again to ${if (c) "<green>enable" else "<red>disable"}<r> it.".parse(true).lie()
            }

            "name" / "toggle" / "all" {
                val a = map.keys.any(map1::contains)

                if (!bool && a) {
                    "<#CBA6F7>[Aerii]<r> Are you sure you want to <red>disable ALL donator names?<r> Run this command again to confirm :(".parse(true).lie()
                    bool = true
                    return@invoke
                }

                for ((k, v) in map) {
                    if (a) remove(k)
                    else put(k, v.string, v, v.visualOrderText)
                }

                build()
                bool = false
                "<#CBA6F7>[Aerii] ${if (a) "<red>Disabled" else "<green>Enabled"}<r> all donator names. Run this command again to ${if (a) "<green>enable" else "<red>disable"}<r> them.".parse(true).lie()
            }

            "name" / "list" {
                "<#CBA6F7>[Aerii] <r>Donator names:".parse(true).lie()
                for ((a, b) in map2) " <dark_gray>• <r>$a <gray>-> ".parse().skip().append(b.toComponent()).lie()
            }

            "name" / "list" / string("search") {
                val s = string("search")
                var i = 0

                "<#CBA6F7>[Aerii] <r>Donator names containing <green>\"$s\"<r>:".parse(true).skip().lie()
                for ((a, b) in map2) {
                    val c = b.toComponent()
                    if (!a.contains(s, true) && !c.string.contains(s, true)) continue

                    i++
                    " <dark_gray>• <r>$a <gray>-> ".parse().skip().append(c).lie()
                }

                if (i != 0) return@string
                "<#CBA6F7>[Aerii] <r>Couldn't find any names containing <green>\"$s\"<r>!".parse(true).skip().lie()
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