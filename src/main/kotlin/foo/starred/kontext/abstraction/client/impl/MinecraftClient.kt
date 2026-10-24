package foo.starred.kontext.abstraction.client.impl

import net.minecraft.client.Minecraft

//? if >= 26.3 {
import com.mojang.blaze3d.Blaze3D
import java.net.URI
//? } else {
/*import net.minecraft.util.Util
*///? }

object MinecraftClient {
    val instance: Minecraft = Minecraft.getInstance()

    var clipboard: String
        get() = instance.keyboardHandler.clipboard
        set(value) {
            instance.keyboardHandler.clipboard = value
        }

    fun url(url: String) {
        //? if >= 26.3 {
        Blaze3D.openUri(URI(url))
        //? } else {
        /*Util.getPlatform().openUri(url)
        *///? }
    }
}
