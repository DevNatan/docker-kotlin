package org.katan.yoki.util

import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import org.katan.yoki.DockerEngine

public expect fun createHttpClient(engine: DockerEngine): HttpClient

// https://stackoverflow.com/a/65579343
public inline fun <T> HttpClient.requestCatching(
    block: HttpClient.() -> T,
    handle: ResponseException.() -> T
): T = runCatching { block() }.getOrElse {
    when (it) {
        is ResponseException -> it.handle()
        else -> throw it
    }
}