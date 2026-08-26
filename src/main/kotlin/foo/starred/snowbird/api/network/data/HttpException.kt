package foo.starred.snowbird.api.network.data

data class HttpException(
    override val message: String,
    val statusCode: Int
) : Exception(message)
