package foo.starred.kontext.api.item.resolver.base

import net.minecraft.world.item.ItemStack

open class LambdaKontextItemDataResolver<T>(private val resolver: (ItemStack) -> T?, cache: Boolean = false) : IKontextItemDataResolver<T>(cache) {
    override fun resolve(stack: ItemStack): T? {
        return resolver(stack)
    }
}
