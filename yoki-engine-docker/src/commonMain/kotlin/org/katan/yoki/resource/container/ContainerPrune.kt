package org.katan.yoki.resource.container

import kotlinx.serialization.*

@Serializable
public data class ContainerPruneFilters(
    public var until: String? = null,
    public var label: String? = null
)

@Serializable
public data class ContainerPruneResult internal constructor(
    @SerialName("ContainersDeleted") public val deletedContainers: List<String>,
    @SerialName("SpaceReclaimed") public val reclaimedSpace: Long
)
