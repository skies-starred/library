package foo.starred.kontext.api.item.extension

import foo.starred.kontext.mixin.accessors.CustomDataAccessor
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import kotlin.jvm.optionals.getOrNull

fun ItemStack.customData(): CompoundTag? {
    return (get(DataComponents.CUSTOM_DATA) as? CustomDataAccessor)?.tag()
}

@Deprecated("Use GenericKontextItemDataResolver.ID")
fun ItemStack.id(): String? {
    return customData()?.getString("id")?.getOrNull()
}

@Deprecated("Use GenericKontextItemDataResolver.UUID")
fun ItemStack.uuid(): String? {
    return customData()?.getString("uuid")?.getOrNull()
}
