package org.katan.yoki.util

import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import org.katan.yoki.DockerEngine
import org.katan.yoki.YokiResourceException

private val resourceExceptionJsonDeserializer = Json {
    ignoreUnknownKeys = true
}

public expect fun createHttpClient(engine: DockerEngine): HttpClient

// https://stackoverflow.com/a/65579343
public suspend inline fun <T> HttpClient.requestCatching(
    block: HttpClient.() -> T,
    handle: ResponseException.() -> Unit
): Result<T> = runCatching { block() }.recover {
    when (it) {
        is ResponseException -> {
            try {
                // there's a missing else branch in when expression if it continues
                it.handle()
            } catch (e: YokiResourceException) {
                return Result.failure(e)
            }

            // unhandled exception
            throw YokiResourceException(it.response.content.readUTF8Line(), it, mapOf())
        }
        else -> throw it
    }
}
