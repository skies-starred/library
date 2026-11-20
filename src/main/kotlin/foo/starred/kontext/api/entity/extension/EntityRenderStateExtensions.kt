package foo.starred.kontext.api.entity.extension

import foo.starred.kontext.api.entity.duck.EntityRenderStateDuck
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.world.entity.Entity

inline val EntityRenderState.entity: Entity?
    get() = (this as? EntityRenderStateDuck)?.`kontext$entity`()

inline fun <reified T : Entity> EntityRenderState.entity(): T? {
    return entity as? T
}
