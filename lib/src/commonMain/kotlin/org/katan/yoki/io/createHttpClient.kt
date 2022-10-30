package org.katan.yoki.io

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
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
            handleResponseExceptionWithRequest { exception, request ->
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
    }
}
