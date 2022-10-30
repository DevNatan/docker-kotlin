package org.katan.yoki.io

import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import org.katan.yoki.YokiResponseException

internal fun <T> Result<T>.mapFailureToHttpStatus(
    statuses: Map<HttpStatusCode, (YokiResponseException) -> Throwable>
) = onFailure { exception ->
    if (exception !is YokiResponseException)
        throw exception

    val resourceException = statuses.entries.firstOrNull { (code, _) ->
        code == exception.statusCode
    }?.value

    throw resourceException
        ?.invoke(exception)
        ?.also { it.addSuppressed(exception) }
        ?: exception
}

internal inline fun requestCatching(
    vararg errors: Pair<HttpStatusCode, (YokiResponseException) -> Throwable>,
    request: () -> HttpResponse
) = runCatching(request).mapFailureToHttpStatus(errors.toMap()).getOrThrow()
