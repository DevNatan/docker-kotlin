package me.devnatan.yoki.resource.image

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.preparePost
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.devnatan.yoki.models.image.ImagePull
import me.devnatan.yoki.models.image.ImageSummary

public class ImageResource internal constructor(
    private val httpClient: HttpClient,
    private val json: Json,
) {

    public companion object {
        private const val BASE_PATH = "/images"
    }

    public suspend fun list(): List<ImageSummary> {
        return httpClient.get("$BASE_PATH/json").body()
    }

    public fun pull(image: String): Flow<ImagePull> = flow {
        httpClient.preparePost("$BASE_PATH/create") {
            parameter("fromImage", image)
        }.execute { response ->
            val channel = response.body<ByteReadChannel>()
            while (true) {
                emit(json.decodeFromString(channel.readUTF8Line() ?: break))
            }
        }
    }

    public suspend fun remove(name: String, force: Boolean? = false, noprune: Boolean? = false) {
        httpClient.delete("$BASE_PATH/$name") {
            parameter("force", force)
            parameter("noprune", noprune)
        }
    }
}
