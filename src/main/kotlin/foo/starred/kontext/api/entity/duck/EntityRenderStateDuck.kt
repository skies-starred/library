@file:Suppress("FunctionName")

package foo.starred.kontext.api.entity.duck

import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.world.entity.Entity

interface EntityRenderStateDuck {
    fun `kontext$entity`(): Entity?
    fun `kontext$entity`(entity: Entity?)

    companion object {
        fun get(state: EntityRenderState?): Entity? {
            return (state as? EntityRenderStateDuck)?.`kontext$entity`()
        }
    }
}
