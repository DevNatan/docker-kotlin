package org.katan.yoki.io

import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.utils.io.readUTF8Line
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.katan.yoki.YokiResourceException

internal val resourceExceptionJsonDeserializer = Json {
    ignoreUnknownKeys = true
}

// https://stackoverflow.com/a/65579343
public inline fun <T> HttpClient.requestCatching(
    request: HttpClient.() -> T,
    handle: ResponseException.() -> T
): Result<T> = runCatching { request() }.recover {
    when (it) {
        is ResponseException -> it.handle()
        else -> throw it
    }
}

public suspend fun <T : YokiResourceException> ResponseException.throwResourceException(
    factory: (String?, Throwable?, Map<String, Any?>) -> T,
    properties: Map<String, Any?>
): Nothing {
    throw factory(
        response.content.readUTF8Line()?.let { content ->
            (resourceExceptionJsonDeserializer.decodeFromString(content) as Map<String, String?>)["message"]
        },
        this, properties
    )
}
