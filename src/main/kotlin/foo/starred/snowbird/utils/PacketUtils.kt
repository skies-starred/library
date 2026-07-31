@file:Suppress("Unused")

package foo.starred.snowbird.utils

import foo.starred.snowbird.api.client
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ServerGamePacketListener

fun Packet<ServerGamePacketListener>.send() {
    client.connection?.send(this)
}