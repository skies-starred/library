@file:Suppress("FunctionName")

package foo.starred.kontext.core.duck

import net.minecraft.world.entity.Entity

interface AvatarRenderStateDuck {
    fun `kontext$entity`(): Entity?
    fun `kontext$entity`(entity: Entity?)
}
