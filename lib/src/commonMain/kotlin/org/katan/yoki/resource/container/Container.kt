package org.katan.yoki.resource.container

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Container internal constructor(
    @SerialName("Id") val id: String,
    @SerialName("Created") val createdString: String,
    @SerialName("Path") val path: String? = null,
    @SerialName("Args") val args: List<String> = emptyList(),
    @SerialName("State") val state: ContainerState,
    @SerialName("Image") val image: String,
    @SerialName("ResolvConfPath") val resolvConfPath: String? = null,
    @SerialName("HostnamePath") val hostnamePath: String? = null,
    @SerialName("HostsPath") val hostsPath: String? = null,
    @SerialName("LogPath") val logsPath: String? = null,
    @SerialName("Name") val name: String,
    @SerialName("RestartCount") val restartCount: Int,
    @SerialName("Driver") val driver: String,
    @SerialName("Platform") val platform: String,
    @SerialName("Mountlabel") val mountLabel: String? = null,
    @SerialName("ProcessLabel") val processLabel: String? = null,
    @SerialName("AppArmorProfile") val appArmorProfile: String? = null,
    @SerialName("ExecIDs") val execIds: List<String>? = null,
    @SerialName("SizeRw") val sizeRw: Long? = null,
    @SerialName("SizeRootFs") val sizeRootFs: Long? = null,
) {

    val createdAt: Instant by lazy { Instant.parse(createdString) }
}
