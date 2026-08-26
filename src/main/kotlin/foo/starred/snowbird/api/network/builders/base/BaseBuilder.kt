package foo.starred.snowbird.api.network.builders.base

import foo.starred.snowbird.Snowbird.SCOPE
import foo.starred.snowbird.api.network.data.HttpException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.logging.log4j.Logger
import java.net.SocketTimeoutException
import kotlin.time.Duration.Companion.milliseconds

abstract class BaseBuilder(val log: Boolean, val logger: Logger) {
    abstract suspend fun run()
    abstract val message: String
    abstract val error: (Exception) -> Unit

    fun execute() = SCOPE.launch {
        if (log) logger.info(message)

        runCatching {
            retry({ it is SocketTimeoutException || (it is HttpException && it.statusCode.retryable()) }, logger) {
                run()
            }
        }.onFailure {
            logger.error(message, it)
            error(it as? Exception ?: Exception(it))
        }
    }

    companion object {
        private val set = setOf(408, 429, 500, 502, 503, 504)

        private fun Int.retryable(): Boolean {
            return this in set
        }

        private suspend fun <T> retry(sr: (Exception) -> Boolean = { true }, logger: Logger, fn: suspend () -> T): T {
            var d = 2000L

            repeat(3) { attempt ->
                try {
                    return fn()
                } catch (e: Exception) {
                    if (attempt == 2) throw e
                    if (!sr(e)) throw e

                    logger.warn("Retrying operation (attempt ${attempt + 1}/3) due to ${e::class.simpleName}: ${e.message}")
                    delay(d.coerceAtMost(30000).milliseconds)
                    d *= 2
                }
            }

            throw IllegalStateException("Retry exhausted")
        }
    }
}
