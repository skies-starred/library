@file:Suppress("Unused")

package xyz.aerii.library.api

import net.minecraft.network.chat.Component
import net.minecraft.world.phys.AABB
import xyz.aerii.library.handlers.Observable
import java.util.*

@JvmField
val EMPTY_UUID = UUID(0, 0)

@JvmField
val EMPTY_COMPONENT = Component.literal("") as Component

@JvmField
val EMPTY_OPTIONAL = Optional.empty<Any>()

@JvmField
val ALWAYS_TRUE = Observable(true).immutable()

@JvmField
val ALWAYS_FALSE = Observable(false).immutable()

@JvmField
val ZERO_PAIR = 0 to 0

@JvmField
val ZERO_AABB = AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)