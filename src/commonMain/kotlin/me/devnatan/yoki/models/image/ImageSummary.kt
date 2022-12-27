package me.devnatan.yoki.models.image

import kotlinx.datetime.Instant
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ImageSummary(
    @SerialName("Id") @Required public val id: String,
    @SerialName("ParentId") @Required public val parentId: String,
    @SerialName("RepoTags") @Required public val repositoryTags: List<String>,
    @SerialName("RepoDigests") @Required public val repositoryDigests: List<String>,
    @SerialName("Created") @Required public val created: Int,
    @SerialName("Size") @Required public val size: Long,
    @SerialName("SharedSize") @Required public val sharedSize: Int,
    @SerialName("VirtualSize") @Required public val virtualSize: Long,
    @SerialName("Labels") @Required public val labels: Map<String, String>?,
    @SerialName("Containers") @Required public val containers: Int,
)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public val ImageSummary.created: Instant
    get() = Instant.fromEpochMilliseconds(created.toLong())
