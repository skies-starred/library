package foo.starred.kontext.api.item.resolver.impl

import foo.starred.kontext.api.item.data.enchant.SkyBlockItemEnchant
import foo.starred.kontext.api.item.resolver.base.IKontextItemDataResolver
import foo.starred.kontext.core.annotations.loader.LoadableKontextModule
import net.minecraft.world.item.ItemStack
import java.util.*
import kotlin.jvm.optionals.getOrDefault
import kotlin.jvm.optionals.getOrNull

@LoadableKontextModule
object SkyBlockEnchantKontextItemDataResolver : IKontextItemDataResolver<List<SkyBlockItemEnchant>>() {
    override fun resolve(stack: ItemStack): List<SkyBlockItemEnchant>? {
        val tag = stack.customData()?.getCompound("enchantments")?.getOrNull() ?: return null

        return ArrayList<SkyBlockItemEnchant>(tag.size()).apply {
            for (key in tag.keySet()) {
                val id = key.lowercase(Locale.ROOT)
                val level = tag.getInt(key).getOrDefault(0)

                add(SkyBlockItemEnchant(id, level))
            }
        }
    }
}
