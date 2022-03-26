package org.katan.yoki.resource.volume

import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.katan.yoki.DockerEngine
import org.katan.yoki.model.volume.Volume
import kotlin.jvm.JvmOverloads

/**
 * @see VolumeResource
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
     * @param filters the filters to use while listing.
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
     * @param config the volume schema to be used while creating.
     * @return Volume
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeCreate">VolumeCreate</a>
     */
    public suspend fun create(config: VolumeConfig): Volume {
        return engine.httpClient.post("$BASE_PATH/create") {
            body = config
        }
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
        return engine.httpClient.get("$BASE_PATH/$id")
    }

    /**
     * Remove a volume
     *
     * @param options the options to be used while removing.
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeDelete">VolumeDelete</a>
     */
    @JvmOverloads
    public suspend fun remove(id: String, options: VolumeRemove? = null) {
        engine.httpClient.delete<Unit>("$BASE_PATH/$id") {
            options?.let { parameter(DELETE_FORCE, it) }
        }
    }

    /**
     * Delete unused volumes
     *
     * @param options he options to be used while pruning.
     * @return VolumePruneResponse
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumePrune">VolumePrune</a>
     */
    @JvmOverloads
    public suspend fun prune(options: VolumePrune? = null): VolumePruneResponse {
        return engine.httpClient.post("$BASE_PATH/prune") {
            options?.let { parameter(PRUNE_FILTERS, options) }
        }
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
public suspend inline fun VolumeResource.list(filters: VolumeFilters.() -> Unit): VolumeListResponse {
    return list(VolumeFilters().apply(filters))
}

/**
 * Create a volume
 *
 * @param config the volume schema to be used while creating.
 * @return Volume
 *
 * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeCreate">VolumeCreate</a>
 */
public suspend inline fun VolumeResource.create(config: VolumeConfig.() -> Unit = {}): Volume {
    return create(VolumeConfig().apply(config))
}

/**
 * Remove a volume
 *
 * @param options the options to be used while removing.
 *
 * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeDelete">VolumeDelete</a>
 */
public suspend inline fun VolumeResource.remove(id: String, options: VolumeRemove.() -> Unit) {
    remove(id, VolumeRemove().apply(options))
}

/**
 * Delete unused volumes
 *
 * @param options he options to be used while pruning.
 * @return VolumePruneResponse
 *
 * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumePrune">VolumePrune</a>
 */
public suspend inline fun VolumeResource.prune(options: VolumePrune.() -> Unit): VolumePruneResponse {
    return prune(VolumePrune().apply(options))
}

/**
 * Volume list filters.
 *
 * @property dangling When set to `true`, returns all volumes that are not in use by a container.
 * When set to `false`, only volumes that are in use by one or more containers are returned.
 * @property driver Matches a network's driver.
 * @property label matches based on the presence of a label alone or a label and a value.
 * @property name Matches all or part of a network name.
 * @see VolumeResource.list
 */
@Serializable
public data class VolumeFilters(
    public var dangling: Boolean = false,
    public var driver: String? = null,
    public var label: String? = null,
    public var name: String? = null,
)

/**
 * Volume configuration.
 *
 * @property name The volume's name.
 * @property driver Name of the volume driver to use.
 * @property driverOpts A mapping of driver options and values.
 * @property labels User-defined key/value metadata.
 * @see VolumeResource.create
 */
@Serializable
public data class VolumeConfig(
    @SerialName("Name") public var name: String? = null,
    @SerialName("Driver") public var driver: String? = null,
    @SerialName("DriverOpts") public var driverOpts: Map<String, String>? = null,
    @SerialName("Labels") public var labels: Map<String, String>? = null
)

/**
 * Volume remove options.
 *
 * @property force When set to `true` volumes will be removed even if they are in use by containers.
 * @see VolumeResource.remove
 */
public data class VolumeRemove(
    var force: Boolean = false
)

/**
 * Volume prune options.
 *
 * @property label Matches volumes that use a label.
 * @see VolumeResource.remove
 */
@Serializable
public data class VolumePrune(
    public var label: String? = null
)

/**
 * Volume list response.
 *
 * @property volumes List of volumes.
 * @property warnings Warnings that occurred when fetching the list of volumes.
 * @see VolumeResource.list
 */
@Serializable
public data class VolumeListResponse(
    @SerialName("Volumes") public val volumes: Collection<Volume>,
    @SerialName("Warnings") public val warnings: Collection<String>?
)

/**
 * Volume prune response.
 *
 * @property volumesDeleted List of deleted volumes ids.
 * @property spaceReclaimed Disk space reclaimed.
 * @see VolumeResource.prune
 */
@Serializable
public data class VolumePruneResponse(
    @SerialName("VolumesDeleted") public val volumesDeleted: Collection<String>?,
    @SerialName("SpaceReclaimed") public val spaceReclaimed: Long
)
