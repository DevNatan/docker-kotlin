package org.katan.yoki.engine.docker.resource.volume

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.katan.yoki.engine.docker.model.volume.Volume

@Serializable
public data class VolumeListResponse(
    @SerialName("Volumes") public val volumes: Collection<Volume>,
    @SerialName("Warnings") public val warnings: Collection<String>
)