@file:Suppress("Unused")

package xyz.aerii.library.api

import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import java.util.UUID

val player: LocalPlayer?
    get() = client.player

val name: String
    get() = client.user.name

val uuid: UUID?
    get() = client.user.profileId

val held: ItemStack?
    get() = player?.mainHandItem

val Entity.self: Boolean
    get() = this is LocalPlayer