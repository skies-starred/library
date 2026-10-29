@file:Suppress("UNCHECKED_CAST", "CAST_NEVER_SUCCEEDS")

package foo.starred.kontext.api.item.resolver.base

import foo.starred.kontext.api.item.duck.ItemStackDuck
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import java.util.concurrent.atomic.AtomicInteger

abstract class IKontextItemDataResolver<T>(open val cache: Boolean = true) {
    private val id: Int = counter.getAndIncrement()

    fun get(stack: ItemStack): T? {
        if (stack.isEmpty) return null
        if (!cache) return resolve(stack)

        val duck = stack as? ItemStackDuck ?: return null
        val cached = duck.`kontext$data`(id)
        if (cached != null) {
            return if (cached === NULL) null else cached as T
        }

        return resolve(stack).also { duck.`kontext$data`(id, it ?: NULL) }
    }

    fun set(stack: ItemStack, value: T?) {
        val duck = stack as? ItemStackDuck ?: return
        duck.`kontext$data`(id, value)
    }

    fun invalidate(stack: ItemStack) {
        val duck = stack as? ItemStackDuck ?: return
        duck.`kontext$data`(id, null)
    }

    abstract fun resolve(stack: ItemStack): T?

    companion object {
        private val counter = AtomicInteger(0)
        private val NULL = Any()

        fun <T> create(cache: Boolean = false, resolver: (ItemStack) -> T?): IKontextItemDataResolver<T> {
            return object : IKontextItemDataResolver<T>(cache) {
                override fun resolve(stack: ItemStack): T? {
                    return resolver(stack)
                }
            }
        }
    }
}
