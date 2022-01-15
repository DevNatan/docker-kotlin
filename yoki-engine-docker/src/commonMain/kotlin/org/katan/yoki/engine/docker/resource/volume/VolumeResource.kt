package org.katan.yoki.engine.docker.resource.volume

import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.katan.yoki.engine.docker.*
import org.katan.yoki.engine.docker.model.volume.*
import kotlin.jvm.JvmOverloads

/**
 * @see VolumeResource
 * @author João Victor Gomides Cruz (devwckd)
 */
public class VolumeResource(
    private val engine: DockerEngine
) {

    private companion object {

        const val BASE_PATH = "/volumes"

        const val LIST_FILTERS = "filters"
        const val DELETE_FORCE = "force"
        const val PRUNE_FILTERS = "filters"
    }

    /**
     * List volumes
     *
     * @param options see VolumeList for options.
     * @return VolumeListResponse
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeList">VolumeList</a>
     */
    @JvmOverloads
    public suspend fun list(filters: VolumeFilters? = null): VolumeListResponse {
        return engine.httpClient.get(BASE_PATH) {
            filters?.let { parameter(LIST_FILTERS, Json.Default.encodeToString(it)) }
        }
    }

    /**
     * Create a volume
     *
     * @param options see VolumeCreate for options.
     * @return Volume
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeCreate">VolumeCreate</a>
     */
    @JvmOverloads
    public suspend fun create(config: VolumeConfig): Volume {
        return engine.httpClient.post("$BASE_PATH/create") {
            body = config
        }
    }

    /**
     * Inspect a volume
     *
     * @param options see VolumeInspect for options.
     * @return Volume
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeInspect">VolumeInspect</a>
     */
    @JvmOverloads
    public suspend fun inspect(id: String): Volume {
        return engine.httpClient.get("$BASE_PATH/${id}")
    }

    /**
     * Remove a volume
     *
     * @param options see VolumeDelete for options.
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeDelete">VolumeDelete</a>
     */
    @JvmOverloads
    public suspend fun remove(id: String, options: VolumeRemove? = null) {
        engine.httpClient.delete<Unit>("$BASE_PATH/${id}") {
            options?.let { parameter(DELETE_FORCE, it) }
        }
    }

    /**
     * Delete unused volumes
     *
     * @param options see VolumePrune for options.
     * @return VolumePruneResponse
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumePrune">VolumePrune</a>
     */
    @JvmOverloads
    public suspend fun prune(options: VolumePrune? = null): VolumePruneResponse {
        return engine.httpClient.delete("$BASE_PATH/prune") {
            options?.let { parameter(PRUNE_FILTERS, options) }
        }
    }

}

public suspend inline fun VolumeResource.list(filters: VolumeFilters.() -> Unit): VolumeListResponse {
    return list(VolumeFilters().apply(filters))
}

public suspend inline fun VolumeResource.create(config: VolumeConfig.() -> Unit): Volume {
    return create(VolumeConfig().apply(config))
}

public suspend inline fun VolumeResource.remove(id: String, options: VolumeRemove.() -> Unit) {
    remove(id, VolumeRemove().apply(options))
}

public suspend inline fun VolumeResource.prune(options: VolumePrune.() -> Unit): VolumePruneResponse {
    return prune(VolumePrune().apply(options))
}

@Serializable
public data class VolumeFilters(
    public var dangling: Boolean = false,
    public var driver: String? = null,
    public var label: String? = null,
    public var name: String? = null,
)

public data class VolumeConfig(
    @SerialName("Name") public var name: String? = null,
    @SerialName("Driver") public var driver: String? = null,
    @SerialName("Scope") public var scope: String? = null,
    @SerialName("Mountpoint") public var mountPoint: String? = null,
    @SerialName("CreatedAt") public var createdAt: String? = null,
    @SerialName("Labels") public var labels: Map<String, String>? = null,
    @SerialName("Options") public var options: Map<String, String>? = null
)

public data class VolumeRemove(
    var force: Boolean = false
)

@Serializable
public data class VolumePrune(
    public var label: String? = null
)

@Serializable
public data class VolumeListResponse(
    @SerialName("Volumes") public val volumes: Collection<Volume>,
    @SerialName("Warnings") public val warnings: Collection<String>
)

@Serializable
public data class VolumePruneResponse(
    @SerialName("VolumesDeleted") public val volumesDeleted: Collection<String>,
    @SerialName("SpaceReclaimed") public val spaceReclaimed: Long
)
