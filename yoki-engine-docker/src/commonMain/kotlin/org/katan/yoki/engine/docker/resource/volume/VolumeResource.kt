package org.katan.yoki.engine.docker.resource.volume

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.katan.yoki.engine.docker.DockerEngine
import org.katan.yoki.engine.docker.network.model.volume.*
import org.katan.yoki.engine.docker.network.response.volume.VolumeListResponse
import org.katan.yoki.engine.docker.network.response.volume.VolumePruneResponse

/**
 * @see VolumeResource
 * @author João Victor Gomides Cruz (devwckd)
 */
public class VolumeResource(
    private val engine: DockerEngine
) {

    public suspend fun list(options: Map<String, Any>): VolumeListResponse {
        return engine.httpClient.get("/volumes") {
            options["filter"]?.let {
                parameter(VolumeFilters, options["filter"])
            }
        }.body()
    }

    public suspend fun create(options: Map<String, Any>): Volume {
        require(VolumeDefinition in options) { "Volume Definition is required" }

        return engine.httpClient.post("/volumes/create") {
            setBody(options[VolumeDefinition])
        }.body()
    }

    public suspend fun inspect(options: Map<String, Any>): Volume {
        require(VolumeName in options) { "Volume Name is required" }

        return engine.httpClient.get("/volumes/${options[VolumeName]}").body()
    }

    public suspend fun remove(options: Map<String, Any>) {
        require(VolumeName in options) { "Volume Name is required" }

        engine.httpClient.delete("/volumes/${options[VolumeName]}") {
            options[VolumeForceDelete]?.let {
                parameter(VolumeForceDelete, it)
            }
        }
    }

    public suspend fun prune(options: Map<String, Any>): VolumePruneResponse {
        return engine.httpClient.delete("/volumes/${options[VolumeName]}") {
            options["filter"]?.let {
                parameter(VolumeFilters, options["filter"])
            }
        }.body()
    }
}