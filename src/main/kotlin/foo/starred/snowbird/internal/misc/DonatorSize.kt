package foo.starred.snowbird.internal.misc

import foo.starred.snowbird.Snowbird.LOGGER
import foo.starred.snowbird.internal.ducks.PlayerDuck
import foo.starred.snowbird.internal.web.WebUtils.request
import net.minecraft.world.entity.player.Player
import org.joml.Vector3f

object DonatorSize {
    private var map: Map<String, Vector3f> = emptyMap()

    init {
        "https://data.aerii.xyz/scales.json".request {
            onSuccess<Map<String, List<Double>>> { kv ->
                val map0: MutableMap<String, Vector3f> = mutableMapOf()

                for ((k, v) in kv) {
                    if (v.size != 3) {
                        LOGGER.error("Invalid scale for $k: expected 3 values, got ${v.size}")
                        continue
                    }

                    map0[k] = Vector3f(v[0].toFloat(), v[1].toFloat(), v[2].toFloat())
                }

                map = map0
            }
        }
    }

    @JvmStatic
    fun fn(player: Player): Boolean {
        val a = map[player.name.string] ?: return false
        val b = player as? PlayerDuck ?: return false

        b.`aerii$library$size`(a.x, a.y, a.z)
        return true
    }
}