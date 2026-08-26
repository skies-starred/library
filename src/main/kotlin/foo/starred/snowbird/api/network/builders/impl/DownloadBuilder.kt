package foo.starred.snowbird.api.network.builders.impl

import foo.starred.snowbird.api.network.WebUtils
import foo.starred.snowbird.api.network.builders.base.BaseBuilder
import foo.starred.snowbird.api.network.data.HttpException
import foo.starred.snowbird.api.network.data.HttpRequest
import java.io.File
import java.net.HttpURLConnection
import java.net.URI

class DownloadBuilder(private val url: String, private val output: File, log: Boolean, api: WebUtils) : BaseBuilder(log, api.logger) {
    private val headers: MutableMap<String, String> = mutableMapOf("User-Agent" to "Mozilla/5.0 (${api.name})")
    private var progress: (Long, Long) -> Unit = { _, _ -> }
    private var complete: (File) -> Unit = {}

    override val message: String = "Starting download from $url → ${output.name}"
    override var error: (Exception) -> Unit = {}

    fun headers(vararg pairs: Pair<String, String>) = apply {
        headers += pairs
    }

    fun progress(block: (downloaded: Long, total: Long) -> Unit) = apply {
        progress = block
    }

    fun success(block: (File) -> Unit) = apply {
        complete = block
    }

    fun error(block: (Exception) -> Unit) = apply {
        error = block
    }

    override suspend fun run() {
        val connection = URI(url).toURL().openConnection().apply {
            for ((k, v) in headers) setRequestProperty(k, v)
            connectTimeout = 15_000
            readTimeout = 45_000
        } as HttpURLConnection

        connection.requestMethod = HttpRequest.GET.name

        try {
            if (connection.responseCode !in 200..299) {
                throw HttpException("HTTP ${connection.responseCode}", connection.responseCode)
            }

            val p0 = connection.contentLengthLong
            var p1 = 0L

            connection.inputStream.use { input ->
                output.outputStream().use { output ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        p1 += bytesRead
                        progress(p1, p0)
                    }
                }
            }

            if (log) logger.info("Download complete: ${output.name} (${output.length()} bytes)")
            complete(output)
        } finally {
            connection.disconnect()
        }
    }
}
