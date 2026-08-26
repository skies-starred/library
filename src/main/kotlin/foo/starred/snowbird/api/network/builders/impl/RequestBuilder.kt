package foo.starred.snowbird.api.network.builders.impl

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import foo.starred.snowbird.Snowbird.GSON
import foo.starred.snowbird.api.network.WebUtils
import foo.starred.snowbird.api.network.builders.base.BaseBuilder
import foo.starred.snowbird.api.network.data.HttpException
import java.net.HttpURLConnection
import java.net.URI
import java.util.zip.GZIPInputStream

class RequestBuilder(private val uri: String, private val method: String, log: Boolean, api: WebUtils) : BaseBuilder(log, api.logger) {
    val headers: MutableMap<String, String> = mutableMapOf("User-Agent" to "Mozilla/5.0 (${api.name})")
    var success: (String) -> Unit = {}
    var body: String? = null

    override val message: String = "Sent $method request to $uri"
    override var error: (Exception) -> Unit = {}

    fun headers(vararg pairs: Pair<String, String>) = apply {
        headers += pairs
    }

    fun body(data: Any) = apply {
        body = data as? String ?: GSON.toJson(data)
        headers["Content-Type"] = "application/json"
    }

    inline fun <reified T> success(noinline block: (T) -> Unit) = apply {
        success = { response ->
            block(
                when (T::class) {
                    String::class -> response as T
                    JsonObject::class -> JsonParser.parseString(response).asJsonObject as T
                    else -> GSON.fromJson(response, object : TypeToken<T>() {}.type)
                }
            )
        }
    }

    fun error(block: (Exception) -> Unit) = apply {
        error = block
    }

    override suspend fun run() {
        val connection = connect()

        try {
            if (body != null && method in set) {
                connection.doOutput = true
                connection.outputStream.use { it.write(body!!.toByteArray()) }
            }

            val int = connection.responseCode
            if (int in 200..299) {
                val stream = connection.inputStream
                val response = (if ("gzip=true" in uri || connection.contentEncoding.equals("gzip", true)) GZIPInputStream(stream) else stream).bufferedReader().use { it.readText() }

                if (log) logger.info("Success in $method for $uri → $int (${response.length} bytes)")
                success(response)
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

    companion object {
        private val set = setOf("POST", "PUT", "PATCH")
    }
}
