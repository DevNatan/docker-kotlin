package me.devnatan.yoki.io

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import me.devnatan.yoki.Yoki

internal actual fun <T : HttpClientEngineConfig> HttpClientConfig<out T>.configureHttpClient(client: Yoki) {
    TODO("Native HTTP client is not supported for now")
}
