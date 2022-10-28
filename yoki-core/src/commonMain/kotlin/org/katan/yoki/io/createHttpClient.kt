package org.katan.yoki.io

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.features.UserAgent
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encodeUtf8
import org.katan.yoki.Yoki
import org.katan.yoki.YokiConfig

internal expect fun <T : HttpClientEngineConfig> HttpClientConfig<out T>.configureHttpClient()

private fun checkSocketPath(config: YokiConfig) {
    check(config.socketPath.isNotBlank()) { "Socket path cannot be blank" }
}

public fun createHttpClient(client: Yoki): HttpClient {
    checkSocketPath(client.config)

    // cannot use CIO due to a Ktor Client bug related to data streaming
    // https://youtrack.jetbrains.com/issue/KTOR-2494
    return HttpClient {
        configureHttpClient()
        defaultRequest {
            contentType(ContentType.Application.Json)

            // workaround for URL prepending
            // https://github.com/ktorio/ktor/issues/537#issuecomment-603272476
            url.takeFrom(
                URLBuilder().takeFrom(
                    URLBuilder(
                        protocol = URLProtocol.HTTP,
                        host = "/var/run/docker.sock".encodeUtf8().hex() + ".socket",
                        port = 2375,
                        encodedPath = "/v${client.config.apiVersion}/"
                    )
                ).apply {
                    encodedPath += url.encodedPath
                }
            )
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(client.json)
        }
        install(UserAgent) { agent = "Yoki/0.0.1" }
    }
}
