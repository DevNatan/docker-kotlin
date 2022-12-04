@file:JvmName("HttpJVM")

package me.devnatan.yoki.net

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.OkHttpConfig
import me.devnatan.yoki.Yoki
import java.util.concurrent.TimeUnit

internal actual fun <T : HttpClientEngineConfig> HttpClientConfig<out T>.configureHttpClient(
    client: Yoki,
) {
    engine {
        // ensure that current engine is OkHttp, cannot use CIO due to a Ktor Client bug related to data streaming
        // https://youtrack.jetbrains.com/issue/KTOR-2494
        require(this is OkHttpConfig) { "Only OkHttp engine is supported for now" }

        config {
            val isUnixSocket = isUnixSocket(client.config.socketPath)
            if (isUnixSocket) {
                socketFactory(UnixSocketFactory())
            }
            dns(SocketDns(isUnixSocket))
            readTimeout(0, TimeUnit.MILLISECONDS)
            connectTimeout(0, TimeUnit.MILLISECONDS)
            callTimeout(0, TimeUnit.MILLISECONDS)
            retryOnConnectionFailure(true)
        }
    }
}
