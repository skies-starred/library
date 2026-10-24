@file:Suppress("UNUSED")

package foo.starred.kontext.api.entity.extension

import foo.starred.kontext.api.entity.duck.LivingEntityDuck
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes

val LivingEntity.serverHealth: Float
    get() = (this as? LivingEntityDuck)?.`kontext$serverHealth`() ?: health

val LivingEntity.serverMaxHealth: Float
    get() = getAttribute(Attributes.MAX_HEALTH)?.baseValue?.toFloat() ?: maxHealth
