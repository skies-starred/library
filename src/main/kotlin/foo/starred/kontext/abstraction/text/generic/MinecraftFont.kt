package foo.starred.kontext.abstraction.text.generic

import foo.starred.kontext.abstraction.client.extension.client
import net.minecraft.client.gui.Font

object MinecraftFont {
    val instance: Font
        get() = client.font

    val instanceNullable: Font?
        get() = client.font
}
