package org.katan.yoki.engine.docker.network.model

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