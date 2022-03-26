package org.katan.yoki.resource.image

import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpStatement
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.katan.yoki.DockerEngine
import org.katan.yoki.model.image.Image
import org.katan.yoki.model.image.ImagePull

public class ImageResource(private val engine: DockerEngine) {

    public companion object {
        private const val BASE_PATH = "/images"
        private val json: Json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    public suspend fun list(): List<Image> {
        return engine.httpClient.get("$BASE_PATH/json")
    }

    public fun pull(image: String): Flow<ImagePull> = flow {
        engine.httpClient.post<HttpStatement>("$BASE_PATH/create") {
            parameter("fromImage", image)
        }.execute { response ->
            val channel = response.content
            while (true) {
                emit(json.decodeFromString(channel.readUTF8Line() ?: break))
            }
        }
    }

    public suspend fun remove(name: String, force: Boolean? = false, noprune: Boolean? = false) {
        return engine.httpClient.delete("$BASE_PATH/$name") {
            force?.let { parameter("force", it) }
            noprune?.let { parameter("noprune", it) }
        }
    }
}
