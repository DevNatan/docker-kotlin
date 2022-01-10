package org.katan.yoki.engine.docker.resource.volume

import io.ktor.client.request.*
import org.katan.yoki.engine.docker.*
import org.katan.yoki.engine.docker.model.volume.*

/**
 * @see VolumeResource
 * @author João Victor Gomides Cruz (devwckd)
 */
public class VolumeResource(
    private val engine: DockerEngine
) {

    /**
     * List volumes
     *
     * @param options see VolumeList for options.
     * @return VolumeListResponse
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeList">VolumeList</a>
     */
    public suspend fun list(options: Map<String, Any>): VolumeListResponse {
        return engine.httpClient.get("/volumes") {
            options["filter"]?.let {
                parameter(VolumeFilters, options["filter"])
            }
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
    public suspend fun create(options: Map<String, Any>): Volume {
        require(VolumeDefinition in options) { "Volume Definition is required" }

        return engine.httpClient.post("/volumes/create") {
            body = options[VolumeDefinition]!!
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
    public suspend fun inspect(options: Map<String, Any>): Volume {
        require(VolumeName in options) { "Volume Name is required" }
        return engine.httpClient.get("/volumes/${options[VolumeName]}")
    }

    /**
     * Remove a volume
     *
     * @param options see VolumeDelete for options.
     *
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/VolumeDelete">VolumeDelete</a>
     */
    public suspend fun remove(options: Map<String, Any>) {
        require(VolumeName in options) { "Volume Name is required" }

        engine.httpClient.delete<Unit>("/volumes/${options[VolumeName]}") {
            options[VolumeForceDelete]?.let {
                parameter(VolumeForceDelete, it)
            }
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
    public suspend fun prune(options: Map<String, Any>): VolumePruneResponse {
        return engine.httpClient.delete("/volumes/${options[VolumeName]}") {
            options[VolumeFilters]?.let {
                parameter(VolumeFilters, options["filter"])
            }
        }
    }

}