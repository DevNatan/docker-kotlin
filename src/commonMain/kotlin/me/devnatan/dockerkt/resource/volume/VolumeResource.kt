package me.devnatan.dockerkt.resource.volume

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.devnatan.dockerkt.models.volume.Volume
import me.devnatan.dockerkt.models.volume.VolumeCreateOptions
import me.devnatan.dockerkt.models.volume.VolumeListOptions
import me.devnatan.dockerkt.models.volume.VolumeListResponse
import me.devnatan.dockerkt.models.volume.VolumePruneOptions
import me.devnatan.dockerkt.models.volume.VolumePruneResponse
import me.devnatan.dockerkt.models.volume.VolumeRemoveOptions
import kotlin.jvm.JvmOverloads

public class VolumeResource internal constructor(
    private val httpClient: HttpClient,
    private val json: Json,
) {
    private companion object {
        const val BASE_PATH = "/volumes"
    }

    @JvmOverloads
    public suspend fun list(options: VolumeListOptions? = null): VolumeListResponse {
        return httpClient.get(BASE_PATH) {
            parameter("filters", options?.let(json::encodeToString))
        }.body()
    }

    /**
     * Create a volume
     *
     * @param config the volume schema to be used while creating.
     * @return Volume
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeCreate">VolumeCreate</a>
     */
    public suspend fun create(config: VolumeCreateOptions): Volume {
        return httpClient.post("$BASE_PATH/create") {
            setBody(config)
        }.body()
    }

    /**
     * Inspect a volume
     *
     * @param id the id of the inspected volume.
     * @return Volume
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeInspect">VolumeInspect</a>
     */
    public suspend fun inspect(id: String): Volume {
        return httpClient.get("$BASE_PATH/$id").body()
    }

    /**
     * Remove a volume
     *
     * @param options the options to be used while removing.
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeDelete">VolumeDelete</a>
     */
    @JvmOverloads
    public suspend fun remove(
        id: String,
        options: VolumeRemoveOptions? = null,
    ) {
        httpClient.delete("$BASE_PATH/$id") {
            parameter("force", options?.force)
        }
    }

    /**
     * Delete unused volumes
     *
     * @param filters The options to be used while pruning.
     * @return VolumePruneResponse
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumePrune">VolumePrune</a>
     */
    @JvmOverloads
    public suspend fun prune(options: VolumePruneOptions? = null): VolumePruneResponse {
        return httpClient.post("$BASE_PATH/prune") {
            parameter("filters", options?.let(json::encodeToString))
        }.body()
    }
}

/**
 * List volumes
 *
 * @param filters the filters to use while listing.
 * @return VolumeListResponse
 *
 * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeList">VolumeList</a>
 */
public suspend inline fun VolumeResource.list(filters: VolumeListOptions.() -> Unit): VolumeListResponse {
    return list(VolumeListOptions().apply(filters))
}

/**
 * Create a volume
 *
 * @param config the volume schema to be used while creating.
 * @return Volume
 *
 * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeCreate">VolumeCreate</a>
 */
public suspend inline fun VolumeResource.create(config: VolumeCreateOptions.() -> Unit = {}): Volume {
    return create(VolumeCreateOptions().apply(config))
}

/**
 * Remove a volume
 *
 * @param options the options to be used while removing.
 *
 * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeDelete">VolumeDelete</a>
 */
public suspend inline fun VolumeResource.remove(
    id: String,
    options: VolumeRemoveOptions.() -> Unit,
) {
    remove(id, VolumeRemoveOptions().apply(options))
}

/**
 * Delete unused volumes
 *
 * @param options he options to be used while pruning.
 * @return VolumePruneResponse
 *
 * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumePrune">VolumePrune</a>
 */
public suspend inline fun VolumeResource.prune(options: VolumePruneOptions.() -> Unit): VolumePruneResponse {
    return prune(VolumePruneOptions().apply(options))
}
