package org.katan.yoki.model.image

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Image(
    @SerialName("Id") public val id: String,
    @SerialName("ParentId") public val parentId: String,
    @SerialName("RepoTags") public val repositoryTags: List<String>,
    @SerialName("RepoDigests") public val repositoryDigests: List<String>,
    @SerialName("Created") public val created: Int,
    @SerialName("Size") public val size: Int,
    @SerialName("SharedSize") public val sharedSize: Int,
    @SerialName("VirtualSize") public val virtualSize: Int,
    @SerialName("Labels") public val labels: Map<String, @Contextual Any?>?,
    @SerialName("Containers") public val containers: Int
)
