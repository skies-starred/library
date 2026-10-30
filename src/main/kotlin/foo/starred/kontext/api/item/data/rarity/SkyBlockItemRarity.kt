package foo.starred.kontext.api.item.data.rarity

import foo.starred.kontext.abstraction.chat.color.MinecraftColors
import foo.starred.kontext.api.format.impl.string.StringFormatter

enum class SkyBlockItemRarity(val color: MinecraftColors) {
    COMMON(MinecraftColors.WHITE),
    UNCOMMON(MinecraftColors.GREEN),
    RARE(MinecraftColors.BLUE),
    EPIC(MinecraftColors.DARK_PURPLE),
    LEGENDARY(MinecraftColors.GOLD),
    MYTHIC(MinecraftColors.PINK),
    DIVINE(MinecraftColors.AQUA),
    ULTIMATE(MinecraftColors.DARK_RED),
    SPECIAL(MinecraftColors.RED),
    VERY_SPECIAL(MinecraftColors.RED),
    UNOBTAINABLE(MinecraftColors.RED);

    val display: String = StringFormatter.titleCased(name).uppercase()

    companion object {
        fun get(string: String): SkyBlockItemRarity? {
            return entries.find { it.display.equals(string, true) }
        }

        fun get(color: Int): SkyBlockItemRarity? {
            return entries.find { it.color.color == color }
        }
    }
}
