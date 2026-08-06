@file:Suppress("FunctionName")

package foo.starred.snowbird.internal.ducks

import net.minecraft.world.entity.Entity

interface AvatarRenderStateDuck {
    fun `snowbird$entity`(): Entity?
    fun `snowbird$entity`(entity: Entity?)
}