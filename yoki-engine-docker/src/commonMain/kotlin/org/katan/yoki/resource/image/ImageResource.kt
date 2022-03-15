package org.katan.yoki.resource.image

import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.katan.yoki.DockerEngine

public class ImageResource(private val engine: DockerEngine) {

    public companion object {
        private const val BASE_PATH = "/images"
    }

    public fun pull(image: String): Flow<String> = flow {
        engine.httpClient.post<HttpStatement>("$BASE_PATH/") {
            parameter("fromImage", image)
        }.execute { response ->
            emit(response.readText())
        }
    }
}
