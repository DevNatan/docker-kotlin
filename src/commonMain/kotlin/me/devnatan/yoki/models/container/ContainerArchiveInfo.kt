package me.devnatan.yoki.models.container

import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ContainerArchiveInfo(
    val name: String,
    val size: Long,
    val mode: Int,
    @SerialName("mtime") val modifiedAtRaw: String,
    val linkTarget: String = "",
)

public val ContainerArchiveInfo.modifiedAt: Instant
    get() = modifiedAtRaw.toInstant()
