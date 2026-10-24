@file:Suppress("UNUSED", "NOTHING_TO_INLINE")

package foo.starred.kontext.api.entity.extension

import foo.starred.kontext.abstraction.client.extension.client
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

inline fun Entity.renderX(): Double {
    return xo + (x - xo) * client.deltaTracker.getGameTimeDeltaPartialTick(true)
}

inline fun Entity.renderY(): Double {
    return yo + (y - yo) * client.deltaTracker.getGameTimeDeltaPartialTick(true)
}

inline fun Entity.renderZ(): Double {
    return zo + (z - zo) * client.deltaTracker.getGameTimeDeltaPartialTick(true)
}

inline fun Entity.renderVec3(): Vec3 {
    return Vec3(renderX(), renderY(), renderZ())
}

inline fun Entity.renderAABB(): AABB {
    return boundingBox.move(renderX() - x, renderY() - y, renderZ() - z)
}
