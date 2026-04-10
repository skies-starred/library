@file:Suppress("Unused", "BlockingMethodInNonBlockingContext")

package xyz.aerii.library.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.logging.log4j.Logger
import xyz.aerii.library.Solstice
import java.io.File
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URI
import java.util.zip.GZIPInputStream

private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
val gson = Gson()

private val set = setOf(408, 429, 500, 502, 503, 504)
private val set0 = setOf("POST", "PUT", "PATCH")
private val Int.retry: Boolean
    get() = this in set

enum class Request {
    GET,
    PUT,
    POST,
    DELETE
}

open class WebUtils(val name: String, val logger: Logger = Solstice.LOGGER) {
    @JvmOverloads
    fun String.request(type: Request = Request.GET, log: Boolean = true, block: RequestBuilder.() -> Unit = {}) {
        RequestBuilder(this, type.name, log).apply(block).execute()
    }

    @JvmOverloads
    fun String.download(output: File, log: Boolean = true, block: DownloadBuilder.() -> Unit = {}) {
        DownloadBuilder(this, output, log).apply(block).execute()
    }

    @JvmOverloads
    suspend fun <T> retry(sr: (Exception) -> Boolean = { true }, fn: suspend () -> T): T {
        var d = 2000L

        repeat(3) { attempt ->
            try {
                return fn()
            } catch (e: Exception) {
                if (attempt == 2 || !sr(e)) throw e
                logger.warn("Retrying operation (attempt ${attempt + 1}/3) due to ${e::class.simpleName}: ${e.message}")
                delay(d.coerceAtMost(30000))
                d *= 2
            }
        }

        throw IllegalStateException("Retry exhausted")
    }

    abstract inner class BaseBuilder(protected val log: Boolean) {
        abstract suspend fun run()
        abstract val message: String
        abstract val onError: (Exception) -> Unit

        fun execute() = scope.launch {
            if (log) logger.info(message)

            runCatching {
                retry({ it is SocketTimeoutException || (it is HttpException && it.statusCode.retry) }) { run() }
            }.onFailure {
                logger.error(message, it)
                onError(it as? Exception ?: Exception(it))
            }
        }
    }

    inner class RequestBuilder(private val uri: String, private val method: String, log: Boolean) : BaseBuilder(log) {
        val headers: MutableMap<String, String> = mutableMapOf("User-Agent" to "Mozilla/5.0 ($name)")
        var body: String? = null
        var onSuccess: (String) -> Unit = {}

        override val message: String = "Sent $method request to $uri"
        override var onError: (Exception) -> Unit = {}

        fun headers(vararg pairs: Pair<String, String>) = apply {
            headers += pairs
        }

        fun body(data: Any) = apply {
            body = data as? String ?: gson.toJson(data)
            headers["Content-Type"] = "application/json"
        }

        inline fun <reified T> onSuccess(noinline block: (T) -> Unit) = apply {
            onSuccess = { response ->
                block(when (T::class) {
                    String::class -> response as T
                    JsonObject::class -> JsonParser.parseString(response).asJsonObject as T
                    else -> gson.fromJson(response, object : TypeToken<T>() {}.type)
                })
            }
        }

        fun onError(block: (Exception) -> Unit) = apply {
            onError = block
        }

        override suspend fun run() {
            val connection = connect()

            try {
                if (body != null && method in set0) {
                    connection.doOutput = true
                    connection.outputStream.use { it.write(body!!.toByteArray()) }
                }

                val int = connection.responseCode
                if (int in 200..299) {
                    val inp = connection.inputStream
                    val response = (if ("gzip=true" in uri) GZIPInputStream(inp) else inp).bufferedReader().use { it.readText() }

                    if (log) logger.info("Success in $method for $uri → $int (${response.length} bytes)")
                    onSuccess(response)
                    return
                }

                logger.warn("Error in $method for $uri → $int")
                val error = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "HTTP $int"
                throw HttpException(error, int)
            } finally {
                connection.disconnect()
            }
        }

        private fun connect(): HttpURLConnection {
            return URI(uri).toURL().openConnection().apply {
                if ("gzip=true" in uri) setRequestProperty("Accept-Encoding", "gzip")

                setRequestProperty("Accept", "*/*")
                for ((k, v) in headers) setRequestProperty(k, v)

                connectTimeout = 15_000
                readTimeout = 45_000

                (this as HttpURLConnection).requestMethod = method
            } as HttpURLConnection
        }
    }

    inner class DownloadBuilder(private val url: String, private val output: File, log: Boolean) : BaseBuilder(log) {
        private val headers = mutableMapOf<String, String>()
        private var onProgress: (Long, Long) -> Unit = { _, _ -> }
        private var onComplete: (File) -> Unit = {}

        override val message: String = "Starting download from $url → ${output.name}"
        override var onError: (Exception) -> Unit = {}

        fun headers(vararg pairs: Pair<String, String>) = apply {
            headers += pairs
        }

        fun onProgress(block: (downloaded: Long, total: Long) -> Unit) = apply {
            onProgress = block
        }

        fun onComplete(block: (File) -> Unit) = apply {
            onComplete = block
        }

        fun onError(block: (Exception) -> Unit) = apply {
            onError = block
        }

        override suspend fun run() {
            val connection = URI(url).toURL().openConnection().apply {
                for ((k, v) in headers) setRequestProperty(k, v)
                connectTimeout = 15_000
                readTimeout = 45_000
            } as HttpURLConnection

            connection.requestMethod = Request.GET.name

            try {
                if (connection.responseCode !in 200..299) throw HttpException("HTTP ${connection.responseCode}", connection.responseCode)

                val p0 = connection.contentLengthLong
                var p1 = 0L

                connection.inputStream.use { input ->
                    output.outputStream().use { output ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int

                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            p1 += bytesRead
                            onProgress(p1, p0)
                        }
                    }
                }

                if (log) logger.info("Download complete: ${output.name} (${output.length()} bytes)")
                onComplete(output)
            } finally {
                connection.disconnect()
            }
        }
    }

    data class HttpException(override val message: String, val statusCode: Int) : Exception(message)
}