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
import okio.ByteString.Companion.encodeUtf8
import org.katan.yoki.Yoki
import java.util.concurrent.TimeUnit

internal actual fun <T : HttpClientEngineConfig> HttpClientConfig<out T>.configureHttpClient(
    client: Yoki
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
    return if (isUnixSocket(socketPath)) {
        URLBuilder(
            protocol = URLProtocol.HTTP,
            port = 2375,
            host = socketPath.substringAfter(UNIX_SOCKET_PREFIX).encodeUtf8().hex() + ".socket"
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
