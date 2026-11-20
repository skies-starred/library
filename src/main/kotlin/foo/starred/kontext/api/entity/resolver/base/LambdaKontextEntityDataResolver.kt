package foo.starred.kontext.api.entity.resolver.base

import net.minecraft.world.entity.Entity

open class LambdaKontextEntityDataResolver<T>(private val resolver: (Entity) -> T?, cache: Boolean = false) : IKontextEntityDataResolver<T>(cache) {
    override fun resolve(entity: Entity): T? {
        return resolver(entity)
    }
}
