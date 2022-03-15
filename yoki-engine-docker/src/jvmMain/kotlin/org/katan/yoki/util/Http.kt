@file:JvmName("JvmHttp")

package org.katan.yoki.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
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
import okhttp3.OkHttpClient
import okio.ByteString.Companion.encodeUtf8
import org.katan.yoki.DockerEngine
import org.katan.yoki.protocol.UnixSocketFactory
import java.util.concurrent.TimeUnit

internal fun OkHttpClient.Builder.configureOkHttpClient() = apply {
    socketFactory(UnixSocketFactory())
    dns(SocketDns())
    readTimeout(0, TimeUnit.MILLISECONDS)
    connectTimeout(0, TimeUnit.MILLISECONDS)
    callTimeout(0, TimeUnit.MILLISECONDS)
}

public actual fun createHttpClient(engine: DockerEngine): HttpClient {
    return HttpClient(OkHttp) {
        engine {
            config {
                configureOkHttpClient()
            }
        }

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
                        encodedPath = "/v1.40/"
                    )
                ).apply {
                    encodedPath += url.encodedPath
                }
            )
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        install(UserAgent) { agent = "Yoki/0.0.1" }
    }
}
