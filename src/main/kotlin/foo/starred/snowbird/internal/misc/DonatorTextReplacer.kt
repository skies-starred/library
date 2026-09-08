package foo.starred.snowbird.internal.misc

import com.google.gson.JsonObject
import foo.starred.kommand.IKommand
import foo.starred.kommand.scopes.KommandCommandScope
import foo.starred.snowbird.Snowbird
import foo.starred.snowbird.api.EMPTY_COMPONENT
import foo.starred.snowbird.api.lie
import foo.starred.snowbird.api.storage.AbstractJsonStore
import foo.starred.snowbird.api.text.parser.impl.parse
import foo.starred.snowbird.api.text.replacer.AbstractTextReplacer
import foo.starred.snowbird.internal.utils.mod
import foo.starred.snowbird.internal.web.WebUtils.request
import foo.starred.snowbird.utils.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.util.FormattedCharSequence

object DonatorTextReplacer : AbstractTextReplacer(), IKommand<FabricClientCommandSource> {
    override val loader: KommandCommandScope<FabricClientCommandSource> = Snowbird.COMMANDS

    @JvmField
    var enabled: AbstractJsonStore.Value<Boolean> = Snowbird.JSON.boolean("donatorName", true)

    init {
        skips = "snowbird_dts_bypass"

        "https://data.starred.foo/donor/names".request {
            success<JsonObject> { json ->
                for ((k, v) in json.entrySet()) {
                    val a = v.asString.parse().withStyle { it.withHoverEvent(HoverEvent.ShowText("<gray>IGN: <red>$k".parse().skip())) }
                    put(k, a.string, a, a.visualOrderText)
                }

                build()
            }
        }

        command("snowbird") {
            "name" / "toggle" {
                val bool = enabled.value
                enabled.value = !bool
                "${if (bool) "<red>Disabled" else "<green>Enabled"}<r> all donator names. Run this command again to ${if (bool) "<green>enable" else "<red>disable"}<r> them.".mod()
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
        return copy().withStyle(style.withInsertion("snowbird_dts_bypass"))
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
