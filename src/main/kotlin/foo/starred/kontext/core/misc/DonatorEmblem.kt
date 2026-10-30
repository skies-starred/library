package foo.starred.kontext.core.misc

import com.google.gson.JsonObject
import foo.starred.kommand.IKommand
import foo.starred.kommand.scopes.KommandCommandScope
import foo.starred.kontext.Kontext
import foo.starred.kontext.api.storage.impl.generic.extension.boolean
import foo.starred.kontext.api.storage.impl.generic.property.base.AbstractStorageProperty
import foo.starred.kontext.api.text.parser.impl.parse
import foo.starred.kontext.core.annotations.loader.LoadableKontextModule
import foo.starred.kontext.core.util.mod
import foo.starred.kontext.core.web.WebUtils.request
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FontDescription
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.Identifier

@LoadableKontextModule
object DonatorEmblem : IKommand<FabricClientCommandSource> {
    private val set = setOf('', '', '', '', '')
    private val font = FontDescription.Resource(Identifier.parse("kontext:emblems"))
    private val emblems = HashMap<String, Component>()

    override val loader: KommandCommandScope<FabricClientCommandSource> = Kontext.COMMANDS

    @JvmField
    var enabled: AbstractStorageProperty<Boolean> = Kontext.JSON.boolean("donatorEmblem", true)

    init {
        "https://data.starred.foo/donor/emblems".request {
            success<JsonObject> { json ->
                for ((k, v) in json.entrySet()) {
                    val v = v.asString
                    emblems[k] = if (set.any { v.contains(it) }) emblem(v) else v.parse()
                }
            }
        }

        command("kontext") {
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
        return copy().withStyle(style.withInsertion("kontext_dts_bypass"))
    }
}
