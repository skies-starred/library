package foo.starred.kontext.api.item.resolver.impl

import foo.starred.kontext.api.item.resolver.base.IKontextItemDataResolver
import foo.starred.kontext.core.annotations.loader.LoadableKontextModule
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack

@LoadableKontextModule
object TextureKontextItemDataResolver : IKontextItemDataResolver<String>() {
    override val cache: Boolean = false

    override fun resolve(stack: ItemStack): String? {
        return stack.get(DataComponents.PROFILE)?.partialProfile()?.properties?.get("textures")?.firstOrNull()?.value()
    }
}
