package me.devnatan.yoki.models.container

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ContainerSummary internal constructor(
    @SerialName("Id") val id: String,
    @SerialName("Names") val names: List<String>? = null,
    @SerialName("Image") val image: String,
    @SerialName("ImageID") val imageCreatedFrom: String? = null,
    @SerialName("Command") val command: String? = null,
    @SerialName("Created") val createdAt: Long,
    // TODO ports
    @SerialName("SizeRw") val sizeRw: Long? = null,
    @SerialName("SizeRootFs") val sizeRootFs: Long? = null,
    @SerialName("Labels") val labels: Map<String, String>,
    @SerialName("State") val state: String,
    @SerialName("Status") val status: String,
    @SerialName("HostConfig") val hostConfig: ContainerSummaryHostConfig,
    // TODO HostConfig, NetworkSettings, Mounts,
) {

    public val name: String = names?.firstOrNull() ?: id
}

@Serializable
public data class ContainerSummaryHostConfig internal constructor(
    @SerialName("NetworkMode") val networkMode: String,
)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public val ContainerSummary.createdAt: Instant
    get() = Instant.fromEpochMilliseconds(createdAt)
