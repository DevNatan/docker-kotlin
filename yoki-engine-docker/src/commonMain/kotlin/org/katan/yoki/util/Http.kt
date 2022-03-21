package org.katan.yoki.util

import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.utils.io.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.katan.yoki.DockerEngine
import org.katan.yoki.YokiResourceException

private val resourceExceptionJsonDeserializer = Json {
    ignoreUnknownKeys = true
}

public expect fun createHttpClient(engine: DockerEngine): HttpClient

// https://stackoverflow.com/a/65579343
public inline fun <T> HttpClient.requestCatching(
    block: HttpClient.() -> T,
    handle: ResponseException.() -> T
): Result<T> = runCatching { block() }.recover {
    when (it) {
        is ResponseException -> it.handle()
        else -> throw it
    }
}

public suspend fun <T : YokiResourceException> ResponseException.throwResourceException(
    factory: (message: String?, cause: Throwable?, properties: Map<String, Any?>) -> T,
    properties: Map<String, Any?>
): Nothing {
    throw factory(
        response.content.readUTF8Line()?.let { content ->
            (resourceExceptionJsonDeserializer.decodeFromString(content) as Map<String, String?>)["message"]
        },
        this, properties
    )
}
