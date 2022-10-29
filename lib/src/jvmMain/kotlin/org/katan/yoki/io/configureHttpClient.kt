package org.katan.yoki.io

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import org.katan.yoki.Yoki
import java.util.concurrent.TimeUnit

internal actual fun <T : HttpClientEngineConfig> HttpClientConfig<out T>.configureHttpClient(
    client: Yoki
) {
    engine {
        require(this is OkHttpConfig) { "Only OkHttp engine is supported for now" }
        config {
            if (isUnixSocket(client.config.socketPath))
                socketFactory(UnixSocketFactory())
            dns(SocketDns())
            readTimeout(0, TimeUnit.MILLISECONDS)
            connectTimeout(0, TimeUnit.MILLISECONDS)
            callTimeout(0, TimeUnit.MILLISECONDS)
            retryOnConnectionFailure(true)
        }
    }

    defaultRequest {
        contentType(ContentType.Application.Json)

        // workaround for URL prepending
        // https://github.com/ktorio/ktor/issues/537#issuecomment-603272476
        url.takeFrom(
            URLBuilder().takeFrom(
                createUrlBuilder(client.config.socketPath)
            ).apply {
                encodedPath = "/v${client.config.apiVersion}/"
                encodedPath += url.encodedPath
            }
        )
    }
}

private fun isUnixSocket(input: String): Boolean {
    return input.startsWith(UNIX_SOCKET_PREFIX)
}

private fun createUrlBuilder(socketPath: String): URLBuilder {
    val url = Url(socketPath)
    return if (isUnixSocket(socketPath)) {
        URLBuilder(
            protocol = URLProtocol.HTTP,
            host = "docker.socket"
        )
    } else {
        URLBuilder(
            protocol = URLProtocol.HTTP,
            host = url.host,
            port = url.port
        )
    }
}
