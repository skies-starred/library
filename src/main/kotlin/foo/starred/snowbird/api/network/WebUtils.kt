@file:Suppress("Unused", "BlockingMethodInNonBlockingContext")

package foo.starred.snowbird.api.network

import foo.starred.snowbird.Snowbird
import foo.starred.snowbird.api.network.builders.impl.DownloadBuilder
import foo.starred.snowbird.api.network.builders.impl.RequestBuilder
import foo.starred.snowbird.api.network.data.HttpRequest
import org.apache.logging.log4j.Logger
import java.io.File

open class WebUtils(val name: String, val logger: Logger = Snowbird.LOGGER) {
    @JvmOverloads
    fun String.request(type: HttpRequest = HttpRequest.GET, log: Boolean = true, block: RequestBuilder.() -> Unit = {}) {
        RequestBuilder(this, type.name, log, this@WebUtils).apply(block).execute()
    }

    @JvmOverloads
    fun String.download(output: File, log: Boolean = true, block: DownloadBuilder.() -> Unit = {}) {
        DownloadBuilder(this, output, log, this@WebUtils).apply(block).execute()
    }
}
