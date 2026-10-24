package foo.starred.kontext.core.misc

import foo.starred.kontext.Kontext.LOGGER
import foo.starred.kontext.core.annotations.loader.LoadableKontextModule
import foo.starred.kontext.core.duck.PlayerDuck
import foo.starred.kontext.core.web.WebUtils.request
import net.minecraft.world.entity.player.Player
import org.joml.Vector3f

@LoadableKontextModule
object DonatorSize {
    private var map: Map<String, Vector3f> = emptyMap()

    init {
        "https://data.starred.foo/donor/sizes".request {
            success<Map<String, List<Double>>> { kv ->
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

        b.`kontext$size`(a.x, a.y, a.z)
        return true
    }
}
