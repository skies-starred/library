@file:Suppress("UNCHECKED_CAST", "CAST_NEVER_SUCCEEDS")

package foo.starred.kontext.api.entity.resolver.base

import foo.starred.kontext.api.entity.duck.EntityDuck
import net.minecraft.world.entity.Entity
import java.util.concurrent.atomic.AtomicInteger

abstract class IKontextEntityDataResolver<T>(open val cache: Boolean = true) {
    private val id: Int = COUNTER.getAndIncrement()

    fun get(entity: Entity): T? {
        if (!cache) return resolve(entity)

        val duck = entity as? EntityDuck ?: return null
        val cached = duck.`kontext$data`(id)
        if (cached != null) {
            return if (cached === NULL) null else cached as T
        }

        return resolve(entity).also { duck.`kontext$data`(id, it ?: NULL) }
    }

    fun set(entity: Entity, value: T?) {
        val duck = entity as? EntityDuck ?: return
        duck.`kontext$data`(id, value)
    }

    fun invalidate(entity: Entity) {
        val duck = entity as? EntityDuck ?: return
        duck.`kontext$data`(id, null)
    }

    abstract fun resolve(entity: Entity): T?

    companion object {
        private val COUNTER = AtomicInteger(0)
        private val NULL = Any()

        fun <T> create(cache: Boolean = false, resolver: (Entity) -> T?): IKontextEntityDataResolver<T> {
            return object : IKontextEntityDataResolver<T>(cache) {
                override fun resolve(entity: Entity): T? {
                    return resolver(entity)
                }
            }
        }
    }
}
