package foo.starred.kontext.api.item.resolver.impl

import foo.starred.kontext.api.item.resolver.base.IKontextItemDataResolver
import foo.starred.kontext.core.annotations.loader.LoadableKontextModule
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

@LoadableKontextModule
object LoreKontextItemDataResolver : IKontextItemDataResolver<List<Component>>() {
    override val cache: Boolean = false

    override fun resolve(stack: ItemStack): List<Component>? {
        return stack.get(DataComponents.LORE)?.styledLines()
    }
}
