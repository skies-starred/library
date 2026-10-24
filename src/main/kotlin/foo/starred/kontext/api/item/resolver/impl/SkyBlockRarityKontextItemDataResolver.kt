@file:Suppress("LocalVariableName")

package foo.starred.kontext.api.item.resolver.impl

import foo.starred.kontext.api.format.extension.string.stripped
import foo.starred.kontext.api.item.data.rarity.SkyBlockItemRarity
import foo.starred.kontext.api.item.extension.get
import foo.starred.kontext.api.item.resolver.base.IKontextItemDataResolver
import foo.starred.kontext.core.annotations.loader.LoadableKontextModule
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

@LoadableKontextModule
object SkyBlockRarityKontextItemDataResolver : IKontextItemDataResolver<SkyBlockItemRarity>() {
    override fun resolve(stack: ItemStack): SkyBlockItemRarity? {
        val lore = stack.get<LoreKontextItemDataResolver, List<Component>>()?.asReversed() ?: return null

        var rarity: SkyBlockItemRarity? = null
        for (line in lore) {
            val stripped = line.stripped().trim().removePrefix("SHINY").trim()
            if (stripped.any { it.isLowerCase() }) continue

            val _rarity = SkyBlockItemRarity.entries.find { stripped.startsWith(it.display) }
            if (_rarity == null) continue

            rarity = _rarity
            break
        }

        return rarity
    }
}
