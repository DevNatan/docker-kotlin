package org.katan.yoki.io

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.katan.yoki.Yoki
import org.katan.yoki.YokiConfig

internal expect fun <T : HttpClientEngineConfig> HttpClientConfig<out T>.configureHttpClient(
    client: Yoki
)

private fun checkSocketPath(config: YokiConfig) {
    check(config.socketPath.isNotBlank()) { "Socket path cannot be blank" }
}

public fun createHttpClient(client: Yoki): HttpClient {
    checkSocketPath(client.config)

    // cannot use CIO due to a Ktor Client bug related to data streaming
    // https://youtrack.jetbrains.com/issue/KTOR-2494
    return HttpClient {
        configureHttpClient(client)
        // install(JsonFeature) {
        //     serializer = KotlinxSerializer(client.json)
        // }
        install(ContentNegotiation) {
            json()
        }
        install(UserAgent) { agent = "Yoki/0.0.1" }
    }
}
