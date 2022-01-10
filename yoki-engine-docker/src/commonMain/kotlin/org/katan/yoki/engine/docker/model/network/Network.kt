package org.katan.yoki.engine.docker.model.network

import kotlinx.serialization.*

@Serializable
public data class Network(
    @SerialName("Id") val id: String,
    @SerialName("Name") val name: String,
    @SerialName("Scope") val scope: String,
    @SerialName("Driver") val driver: String,
    @SerialName("EnableIPV6") val enableIPv6: Boolean,
    @SerialName("Internal") val isInternal: Boolean,
    @SerialName("Attachable") val isAttachable: Boolean,
    @SerialName("Ingress") val ingress: Boolean,
    @SerialName("Containers") val containers: Map<String, NetworkContainer>,
    @SerialName("Options") val options: Map<String, String>,
    @SerialName("Labels") val labels: Map<String, String>
)

public const val NetworkId: String = "id"
public const val NetworkDriver: String = "driver"
public const val NetworkDangling: String = "dangling"
public const val NetworkLabel: String = "label"

public const val NetworkCustomType: String = "custom"
public const val NetworkBuiltinType: String = "builtin"

public const val NetworkLocalScope: String = "local"
public const val NetworkGlobalScope: String = "global"
public const val NetworkSwarmScope: String = "swarm"