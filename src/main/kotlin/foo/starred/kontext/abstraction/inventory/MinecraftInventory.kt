package foo.starred.kontext.abstraction.inventory

import foo.starred.kontext.abstraction.client.impl.MinecraftClient
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.ItemStack

object MinecraftInventory {
    val instance: Inventory?
        get() = MinecraftClient.instance.player?.inventory

    val menuInstance: InventoryMenu?
        get() = MinecraftClient.instance.player?.inventoryMenu

    val items: List<ItemStack>?
        get() = instance?.nonEquipmentItems

    val hotbar: List<ItemStack>?
        get() = items?.take(9)

    var selected: Int
        get() = instance?.selectedSlot ?: -1
        set(value) = instance?.selectedSlot = value
}
