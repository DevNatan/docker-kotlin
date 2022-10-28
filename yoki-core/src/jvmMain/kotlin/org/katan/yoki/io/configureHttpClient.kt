package org.katan.yoki.io

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.OkHttpConfig
import java.util.concurrent.TimeUnit

internal actual fun <T : HttpClientEngineConfig> HttpClientConfig<out T>.configureHttpClient() {
    engine {
        require(this is OkHttpConfig) { "Only OkHttp engine is supported for now" }
        config {
            socketFactory(UnixSocketFactory())
            dns(SocketDns())
            readTimeout(0, TimeUnit.MILLISECONDS)
            connectTimeout(0, TimeUnit.MILLISECONDS)
            callTimeout(0, TimeUnit.MILLISECONDS)
        }
    }
}
