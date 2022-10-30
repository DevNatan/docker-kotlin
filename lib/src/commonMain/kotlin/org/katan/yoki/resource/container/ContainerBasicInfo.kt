package org.katan.yoki.resource.container

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ContainerBasicInfo internal constructor(
    @SerialName("Id") val id: String,
    @SerialName("Image") val image: String,
    @SerialName("ImageID") val imageCreatedFrom: String? = null,
    @SerialName("Names") val names: List<String>? = null,
    @SerialName("Command") val command: String? = null,
    @SerialName("Created") val createdLong: Long,
    @SerialName("SizeRw") val sizeRw: Long? = null,
    @SerialName("SizeRootFs") val sizeRootFs: Long? = null,
    @SerialName("State") val state: String,
    @SerialName("Status") val status: String,
    @SerialName("Labels") val labels: Map<String, String>,
) {

    public val name: String get() = names?.firstOrNull() ?: id

    public val createdAt: Instant
        get() = Instant.fromEpochMilliseconds(createdLong)
}
