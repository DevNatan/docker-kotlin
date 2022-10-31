package org.katan.yoki.net

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.cio.CIOEngineConfig
import org.katan.yoki.Yoki

internal actual fun <T : HttpClientEngineConfig> HttpClientConfig<out T>.configureHttpClient(
    client: Yoki
) {
    engine {
        require(this is CIOEngineConfig)
    }
}
