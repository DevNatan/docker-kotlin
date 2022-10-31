package org.katan.yoki.net

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.statement.HttpResponse
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
import okio.ByteString.Companion.decodeHex
import okio.ByteString.Companion.encodeUtf8
import org.katan.yoki.GenericDockerErrorResponse
import org.katan.yoki.Yoki
import org.katan.yoki.YokiConfig
import org.katan.yoki.YokiResponseException

internal expect fun <T : HttpClientEngineConfig> HttpClientConfig<out T>.configureHttpClient(
    client: Yoki
)

private fun checkSocketPath(config: YokiConfig) {
    check(config.socketPath.isNotBlank()) { "Socket path cannot be blank" }
}

internal fun createHttpClient(client: Yoki): HttpClient {
    checkSocketPath(client.config)
    return HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        // TODO set Yoki version on user agent
        install(UserAgent) { agent = "Yoki/0.0.1" }
        configureHttpClient(client)

        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
                val exceptionResponse = clientException.response

                val error = exceptionResponse.body<GenericDockerErrorResponse>()
                throw YokiResponseException(
                    cause = exception,
                    message = error.message,
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
                }
            )
        }
    }
}

internal fun decodeHostname(hostname: String): String {
    return hostname
        .substring(0, hostname.indexOf(ENCODED_HOSTNAME_SUFFIX))
        .decodeHex()
        .utf8()
}

private fun createUrlBuilder(socketPath: String): URLBuilder {
    return if (isUnixSocket(socketPath)) {
        URLBuilder(
            protocol = URLProtocol.HTTP,
            port = DOCKER_SOCKET_PORT,
            host = socketPath.substringAfter(UNIX_SOCKET_PREFIX).encodeUtf8().hex() + ENCODED_HOSTNAME_SUFFIX
        )
    } else {
        val url = Url(socketPath)
        URLBuilder(
            protocol = URLProtocol.HTTP,
            host = url.host,
            port = url.port
        )
    }
}

internal fun <T> Result<T>.mapFailureToHttpStatus(
    statuses: Map<HttpStatusCode, (YokiResponseException) -> Throwable>
) = onFailure { exception ->
    if (exception !is YokiResponseException)
        throw exception

    val resourceException = statuses.entries.firstOrNull { (code, _) ->
        code == exception.statusCode
    }?.value

    throw resourceException
        ?.invoke(exception)
        ?.also { it.addSuppressed(exception) }
        ?: exception
}

internal inline fun requestCatching(
    vararg errors: Pair<HttpStatusCode, (YokiResponseException) -> Throwable>,
    request: () -> HttpResponse
) = runCatching(request).mapFailureToHttpStatus(errors.toMap()).getOrThrow()
