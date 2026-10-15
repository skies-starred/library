package foo.starred.snowbird.internal.misc

import com.google.gson.JsonObject
import foo.starred.kommand.IKommand
import foo.starred.kommand.scopes.KommandCommandScope
import foo.starred.snowbird.Snowbird
import foo.starred.snowbird.api.storage.AbstractJsonStore
import foo.starred.snowbird.api.text.parser.impl.parse
import foo.starred.snowbird.internal.utils.mod
import foo.starred.snowbird.internal.web.WebUtils.request
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FontDescription
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.Identifier

object DonatorEmblem : IKommand<FabricClientCommandSource> {
    private val font = FontDescription.Resource(Identifier.parse("snowbird:emblems"))
    private val emblems = HashMap<String, Component>()

    override val loader: KommandCommandScope<FabricClientCommandSource> = Snowbird.COMMANDS

    @JvmField
    var enabled: AbstractJsonStore.Value<Boolean> = Snowbird.JSON.boolean("donatorEmblem", true)

    init {
        "https://data.starred.foo/donor/emblems".request {
            success<JsonObject> {
                for ((k, v) in it.entrySet()) {
                    emblems[k] = emblem(v.asString)
                }
            }
        }

        command("snowbird") {
            "emblem" / "toggle" {
                val bool = enabled.value
                enabled.value = !bool
                "${if (bool) "<red>Disabled" else "<green>Enabled"}<r> all donator emblems. Run this command again to ${if (bool) "<green>enable" else "<red>disable"}<r> them.".mod()
            }
        }
    }

    fun fn(name: String): Boolean {
        return emblems.containsKey(name)
    }

    fun fn(name: String, tag: Component): Component {
        val emblem = emblems[name] ?: return tag
        return Component.empty().append(emblem.copy()).append(" <dark_gray>|<r> ".parse().skip()).append(tag)
    }

    private fun emblem(vararg text: String): Component {
        return text.joinToString("").parse().withStyle { it.withFont(font) }
    }

    private fun MutableComponent.skip(): MutableComponent {
        return copy().withStyle(style.withInsertion("snowbird_dts_bypass"))
    }
}
