package foo.starred.kontext.api.item.extension

import com.google.common.collect.ImmutableMultimap
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import com.mojang.authlib.properties.PropertyMap
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ResolvableProfile
import java.util.UUID

fun ItemStack.texture(texture: String): ItemStack {
    if (item == Items.PLAYER_HEAD) {
        val properties = PropertyMap(ImmutableMultimap.of("textures", Property("textures", texture)))
        val profile = GameProfile(UUID.randomUUID(), "", properties)
        set(DataComponents.PROFILE, ResolvableProfile.createResolved(profile))
    }

    return this
}
