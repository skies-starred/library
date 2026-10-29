package foo.starred.kontext.api.item.resolver.impl

import foo.starred.kontext.api.item.extension.customData
import foo.starred.kontext.api.item.resolver.base.IKontextItemDataResolver
import foo.starred.kontext.api.item.resolver.base.LambdaKontextItemDataResolver
import net.minecraft.core.component.DataComponents
import kotlin.collections.firstOrNull
import kotlin.jvm.optionals.getOrNull

object GenericKontextItemDataResolver {
    object ID : LambdaKontextItemDataResolver<String>({ stack ->
        stack.customData()?.getString("id")?.getOrNull()
    })

    object UUID : LambdaKontextItemDataResolver<String>({ stack ->
        stack.customData()?.getString("uuid")?.getOrNull()
    })

    object TEXTURE : LambdaKontextItemDataResolver<String>({ stack ->
        stack.get(DataComponents.PROFILE)?.partialProfile()?.properties?.get("textures")?.firstOrNull()?.value()
    })

    object TIMESTAMP : LambdaKontextItemDataResolver<Long>({ stack ->
        stack.customData()?.getLong("timestamp")?.getOrNull()
    })

    object STARS : LambdaKontextItemDataResolver<Int>({ stack ->
        stack.customData()?.getInt("upgrade_level")?.getOrNull() ?: stack.customData()?.getInt("dungeon_item_level")?.getOrNull()
    })

    object QUALITY : LambdaKontextItemDataResolver<Int>({ stack ->
        stack.customData()?.getInt("baseStatBoostPercentage")?.getOrNull()
    })

    object TIER : LambdaKontextItemDataResolver<Int>({ stack ->
        stack.customData()?.getInt("dungeon_tier")?.getOrNull() ?: stack.customData()?.getInt("item_tier")?.getOrNull()
    })
}
