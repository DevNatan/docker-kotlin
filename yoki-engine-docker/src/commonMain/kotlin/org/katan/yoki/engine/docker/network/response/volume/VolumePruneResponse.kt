package org.katan.yoki.engine.docker.network.response.volume

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.katan.yoki.engine.docker.network.model.volume.Volume

@Serializable
public data class VolumePruneResponse(
    @SerialName("VolumesDeleted") public val volumesDeleted: Collection<String>,
    @SerialName("SpaceReclaimed") public val spaceReclaimed: Long
)