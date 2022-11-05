package org.katan.yoki.net

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import org.katan.yoki.Yoki

internal actual fun <T : HttpClientEngineConfig> HttpClientConfig<out T>.configureHttpClient(
    client: Yoki
) {
    TODO("Native HTTP client is not supported for now")
}
