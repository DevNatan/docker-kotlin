package me.devnatan.yoki.resource.image

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.devnatan.yoki.models.image.ImageBuildOptions
import me.devnatan.yoki.models.image.ImagePull
import me.devnatan.yoki.models.image.ImageSummary

private const val BASE_PATH = "/images"

public class ImageResource internal constructor(
    private val httpClient: HttpClient,
    private val json: Json,
) {

    public companion object {

        private val TAR_CONTENT_TYPE = ContentType.parse("application/x-tar")
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

    /**
     * builds a Docker image using the specified archive path and [ImageBuildOptions].
     *
     * @param archivePath The path to the build context archive (e.g., a TAR file) that contains the source code and resources.
     * @param options The [ImageBuildOptions] containing the configuration for the image build.
     */
    public suspend fun build(archivePath: String, options: ImageBuildOptions) {
        httpClient.post("/build") {
            contentType(TAR_CONTENT_TYPE)
            header("X-Registry-Config", json.encodeToString(options.registryConfig))
            parameter("dockerfile", options.path)
            parameter("t", options.tag)
            parameter("extrahosts", options.extraHosts)
            parameter("remote", options.remote)
            parameter("q", options.suppressVerbose)
            parameter("nocache", options.noCache)
            parameter("cachefrom", options.cacheFrom)
            parameter("pull", options.pull)
            parameter("rm", options.removeIntermediateContainers)
            parameter("forcerm", options.forceRemoveIntermediateContainers)
            parameter("memory", options.memoryLimit)
            parameter("memswap", options.memorySwap)
            parameter("cpushares", options.cpuShares)
            parameter("cpusetcpus", options.cpuSetCpus)
            parameter("cpuperiod", options.cpuPeriod)
            parameter("cpuquota", options.cpuQuota)
            parameter("buildargs", options.buildArgs)
            parameter("shmsize", options.shmSize)
            parameter("squash", options.squash)
            parameter("labels", options.labels)
            parameter("networkmode", options.networkMode)
            parameter("platform", options.platform)
            parameter("target", options.target)
            parameter("outputs", options.outputs)
            parameter("version", options.version)
            setBody(archivePath)
        }
    }
}

public suspend inline fun ImageResource.build(archivePath: String, options: ImageBuildOptions.() -> Unit) {
    build(archivePath, ImageBuildOptions().apply(options))
}
