package foo.starred.kontext.abstraction.player

import foo.starred.kontext.Kontext
import foo.starred.kontext.abstraction.client.impl.MinecraftClient
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import java.util.UUID

object MinecraftPlayer {
    val instance: Player
        get() = instanceNullable!!

    val instanceNullable: Player?
        get() = MinecraftClient.instance.player

    val name: String
        get() = MinecraftClient.instance.user.name

    val uuid: UUID
        get() = MinecraftClient.instance.user.profileId

    val container: AbstractContainerMenu?
        get() = instanceNullable?.containerMenu

    fun authenticate(server: String = UUID.randomUUID().toString(), error: String = "Failed to authenticate with Mojang!"): Pair<Boolean, String> {
        try {
            MinecraftClient.instance.services().sessionService().joinServer(uuid, MinecraftClient.instance.user.accessToken, server)
            return Pair(true, server)
        } catch (e: Exception) {
            Kontext.LOGGER.error(error, e)
            return Pair(false, server)
        }
    }
}
