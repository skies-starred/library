@file:Suppress("Unused", "BlockingMethodInNonBlockingContext")

package foo.starred.kontext.api.network

import foo.starred.kontext.Kontext
import foo.starred.kontext.api.network.builders.impl.DownloadBuilder
import foo.starred.kontext.api.network.builders.impl.RequestBuilder
import foo.starred.kontext.api.network.data.HttpRequest
import org.slf4j.Logger
import java.io.File

open class WebUtils(val name: String, val logger: Logger = Kontext.LOGGER) {
    @JvmOverloads
    fun String.request(type: HttpRequest = HttpRequest.GET, log: Boolean = true, block: RequestBuilder.() -> Unit = {}) {
        RequestBuilder(this, type.name, log, this@WebUtils).apply(block).execute()
    }

    @JvmOverloads
    fun String.download(output: File, log: Boolean = true, block: DownloadBuilder.() -> Unit = {}) {
        DownloadBuilder(this, output, log, this@WebUtils).apply(block).execute()
    }
}
