package me.devnatan.yoki.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class NetworkingConfig(
    @SerialName("EndpointsConfig") public var endpointsConfig: Map<String, EndpointSettings>? = null,
)