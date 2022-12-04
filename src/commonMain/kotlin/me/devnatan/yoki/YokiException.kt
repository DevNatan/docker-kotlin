package me.devnatan.yoki

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable

public open class YokiException internal constructor(cause: Throwable?) : RuntimeException(cause)

public open class YokiResourceException internal constructor(
    cause: Throwable?,
) : YokiException(cause) {
    override val message: String? get() = null
}

public class YokiResponseException internal constructor(
    cause: Throwable?,
    override val message: String?,
    public val statusCode: HttpStatusCode,
) : YokiResourceException(cause)

@Serializable
internal data class GenericDockerErrorResponse(
    val message: String,
)
