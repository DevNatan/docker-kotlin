package me.devnatan.yoki.io

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.devnatan.yoki.GenericDockerErrorResponse
import me.devnatan.yoki.Yoki
import me.devnatan.yoki.YokiResponseException

internal expect fun <T : HttpClientEngineConfig> HttpClientConfig<out T>.configureHttpClient(client: Yoki)

internal fun createHttpClient(client: Yoki): HttpClient {
    check(client.config.socketPath.isNotBlank()) { "Socket path cannot be blank" }
    return HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                },
            )
        }

        if (client.config.debugHttpCalls) {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }

        install(UserAgent) { agent = "Yoki" }
        configureHttpClient(client)

        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                val responseException = exception as? ResponseException ?: return@handleResponseExceptionWithRequest
                val exceptionResponse = responseException.response
                println("exceptionResponse = ${exceptionResponse.body<String>()}")

                val errorMessage = runCatching {
                    exceptionResponse.body<GenericDockerErrorResponse>()
                }.getOrNull()?.message
                throw YokiResponseException(
                    cause = exception,
                    message = errorMessage,
                    statusCode = exceptionResponse.status,
                )
            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)

            // workaround for URL prepending
            // https://github.com/ktorio/ktor/issues/537#issuecomment-603272476
            url.takeFrom(
                URLBuilder(createUrlBuilder(client.config.socketPath)).apply {
                    encodedPath = "/v${client.config.apiVersion}/"
                    encodedPath += url.encodedPath
                },
            )
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun createUrlBuilder(socketPath: String): URLBuilder = if (isUnixSocket(socketPath)) {
    URLBuilder(
        protocol = URLProtocol.HTTP,
        port = DOCKER_SOCKET_PORT,
        host = socketPath.substringAfter(UNIX_SOCKET_PREFIX).encodeToByteArray().toHexString() + ENCODED_HOSTNAME_SUFFIX,
    )
} else {
    val url = Url(socketPath)
    URLBuilder(
        protocol = URLProtocol.HTTP,
        host = url.host,
        port = url.port,
    )
}

internal fun handleHttpFailure(exception: Throwable, statuses: Map<HttpStatusCode, (YokiResponseException) -> Throwable>) {
    if (exception !is YokiResponseException) {
        throw exception
    }

    val resourceException = statuses.entries.firstOrNull { (code, _) ->
        code == exception.statusCode
    }?.value

    throw resourceException
        ?.invoke(exception)
        ?.also { root -> root.addSuppressed(exception) }
        ?: exception
}

// TODO use Ktor exception handler instead
internal inline fun <T> requestCatching(
    vararg errors: Pair<HttpStatusCode, (YokiResponseException) -> Throwable>,
    request: () -> T,
) = runCatching(request).onFailure { exception -> handleHttpFailure(exception, errors.toMap()) }.getOrThrow()
