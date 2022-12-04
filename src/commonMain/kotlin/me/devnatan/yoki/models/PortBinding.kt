package me.devnatan.yoki.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PortBinding(
    @SerialName("HostIp") public var ip: String? = null,
    @SerialName("HostPort") public var port: String? = null,
)
