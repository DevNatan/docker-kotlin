package org.katan.yoki.engine.docker.network.model.volume

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Volume(
    @SerialName("Name") public val name: String,
    @SerialName("Driver") public val driver: String,
    @SerialName("Scope") public val scope: String,
    @SerialName("Mountpoint") public val mountPoint: String,
    @SerialName("CreatedAt") public val createdAt: String?,
    @SerialName("Labels") public val labels: Map<String, String>,
    @SerialName("Options") public val options: Map<String, String>
)

public const val VolumeFilters: String = "filters"

public const val VolumeDefinition: String = "definition"

public const val VolumeName: String = "name"

public const val VolumeForceDelete: String = "forceDelete"