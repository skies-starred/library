package xyz.aerii.library.internal.misc

import com.google.gson.JsonObject
import xyz.aerii.library.handlers.minecraft.AbstractWords
import xyz.aerii.library.handlers.parser.parse
import xyz.aerii.library.internal.web.WebUtils.request

object DonatorWords : AbstractWords() {
    init {
        "https://data.aerii.xyz/donators.json".request {
            onSuccess<JsonObject> { json ->
                for ((k, v) in json.entrySet()) {
                    val a = v.asString.parse()
                    put(k, a.string, a, a.visualOrderText)
                }

                build()
            }
        }
    }
}