@file:Suppress("Unused")

package foo.starred.kontext.api

import foo.starred.kontext.api.data.Observable
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.AABB
import java.util.*

@JvmField
val EMPTY_UUID = UUID(0, 0)

@JvmField
val EMPTY_COMPONENT = Component.empty() as Component

@JvmField
val ALWAYS_TRUE = Observable(true).immutable()

@JvmField
val ALWAYS_FALSE = Observable(false).immutable()

@JvmField
val ZERO_PAIR = 0 to 0

@JvmField
val ZERO_AABB = AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
