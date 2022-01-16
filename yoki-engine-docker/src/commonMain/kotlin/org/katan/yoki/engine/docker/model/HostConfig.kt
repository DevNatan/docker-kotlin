package org.katan.yoki.engine.docker.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class HostConfig(
    @SerialName("NetworkMode") public val networkMode: String,
)
