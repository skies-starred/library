@file:Suppress("LocalVariableName")

package foo.starred.kontext.api.item.resolver.impl

import com.google.gson.JsonParser
import foo.starred.kontext.api.format.extension.string.stripped
import foo.starred.kontext.api.item.data.rarity.SkyBlockItemRarity
import foo.starred.kontext.api.item.extension.customData
import foo.starred.kontext.api.item.extension.get
import foo.starred.kontext.api.item.resolver.base.IKontextItemDataResolver
import foo.starred.kontext.core.annotations.loader.LoadableKontextModule
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import kotlin.jvm.optionals.getOrNull

@LoadableKontextModule
object SkyBlockRarityKontextItemDataResolver : IKontextItemDataResolver<SkyBlockItemRarity>() {
    override fun resolve(stack: ItemStack): SkyBlockItemRarity? {
        val pet = stack.customData()?.getString("petInfo")?.getOrNull()?.let { JsonParser.parseString(it).asJsonObject.get("tier").asString }
        if (pet != null) return SkyBlockItemRarity.get(pet)

        val lore = stack.get<LoreKontextItemDataResolver, List<Component>>()?.asReversed() ?: return null
        var rarity: SkyBlockItemRarity? = null

        for (line in lore) {
            var stripped = line.stripped().trim().removePrefix("SHINY").trim()
            if (stripped.length > 2 && stripped[1].isWhitespace()) stripped = stripped.drop(2)
            if (stripped.length > 2 && stripped[stripped.lastIndex - 1].isWhitespace()) stripped = stripped.dropLast(2)
            if (stripped.any { it.isLowerCase() }) continue

            val _rarity = SkyBlockItemRarity.entries.find { stripped.startsWith(it.display) }
            if (_rarity == null) continue

            rarity = _rarity
            break
        }

        return rarity
    }
}
