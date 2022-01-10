package org.katan.yoki.engine.docker.model.volume

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

/**
 * Filters used in List, Inspect and Prune requests.
 *
 * @see org.katan.yoki.engine.docker.resource.volume.VolumeResource.list
 * @see org.katan.yoki.engine.docker.resource.volume.VolumeResource.inspect
 * @see org.katan.yoki.engine.docker.resource.volume.VolumeResource.prune
 */
public const val VolumeFilters: String = "filters"

/**
 * Volume definition used in Create request.
 *
 * @see org.katan.yoki.engine.docker.resource.volume.VolumeResource.create
 */
public const val VolumeDefinition: String = "definition"

/**
 * Volume's name or ID used in Inspect and Remove requests.
 *
 * @see org.katan.yoki.engine.docker.resource.volume.VolumeResource.inspect
 * @see org.katan.yoki.engine.docker.resource.volume.VolumeResource.remove
 */
public const val VolumeName: String = "name"

/**
 * Parameter used to set if a volume should be force deleted in Remove request
 *
 * @see org.katan.yoki.engine.docker.resource.volume.VolumeResource.remove
 */
public const val VolumeForceDelete: String = "forceDelete"