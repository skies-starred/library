package foo.starred.kontext.api.network.data

data class HttpException(
    override val message: String,
    val statusCode: Int
) : Exception(message)
