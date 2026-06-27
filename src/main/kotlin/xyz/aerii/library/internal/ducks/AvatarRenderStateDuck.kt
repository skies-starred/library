@file:Suppress("FunctionName")

package xyz.aerii.library.internal.ducks

import net.minecraft.world.entity.Entity

interface AvatarRenderStateDuck {
    fun `aerii$library$entity`(): Entity?
    fun `aerii$library$entity`(entity: Entity?)
}