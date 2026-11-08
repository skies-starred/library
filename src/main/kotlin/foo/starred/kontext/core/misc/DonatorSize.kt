package foo.starred.kontext.core.misc

import foo.starred.kommand.IKommand
import foo.starred.kommand.scopes.KommandCommandScope
import foo.starred.kontext.Kontext
import foo.starred.kontext.Kontext.LOGGER
import foo.starred.kontext.core.annotations.loader.LoadableKontextModule
import foo.starred.kontext.core.duck.PlayerDuck
import foo.starred.kontext.core.util.mod
import foo.starred.kontext.core.web.WebUtils.request
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.world.entity.player.Player
import org.joml.Vector3f

@LoadableKontextModule
object DonatorSize : IKommand<FabricClientCommandSource> {
    private var map: Map<String, Vector3f> = emptyMap()

    override val loader: KommandCommandScope<FabricClientCommandSource> = Kontext.COMMANDS

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

        command("kontext") {
            "refresh" / "scale" {
                "Refreshing player scale data...".mod()

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
                        "Successfully reset player scale data".mod()
                    }
                }
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
