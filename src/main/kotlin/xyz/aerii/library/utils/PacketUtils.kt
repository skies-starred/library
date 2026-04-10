@file:Suppress("Unused")

package xyz.aerii.library.utils

import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ServerGamePacketListener
import xyz.aerii.library.api.client

fun Packet<ServerGamePacketListener>.send() {
    client.connection?.send(this)
}