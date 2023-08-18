package me.devnatan.yoki.models.container

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.devnatan.yoki.models.network.EndpointSettings

@Serializable
public data class Container internal constructor(
    @SerialName("Id") val id: String,
    @SerialName("Created") val createdAtRaw: String,
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
    @SerialName("NetworkSettings") val networkSettings: NetworkSettings,
) {

    val createdAt: Instant by lazy { Instant.parse(createdAtRaw) }
}

@Serializable
public data class NetworkSettings internal constructor(
    @SerialName("Bridge") public val bridge: String,
    @SerialName("SandboxID") public val sandboxId: String,
    @SerialName("HairpinMode") public val hairpinMode: Boolean,
    @SerialName("LinkLocalIPv6Address") public val linkLocalIPv6Address: String,
    @SerialName("LinkLocalIPv6PrefixLen") public val linkLocalIPv6PrefixLength: Int,
    @SerialName("GlobalIPv6Address") public val globalIPv6Address: String? = null,
    @SerialName("GlobalIPv6PrefixLen") public val globalIPv6PrefixLength: Int? = null,
    @SerialName("IPAddress") public val ipAddress: String? = null,
    @SerialName("IPPrefixLen") public val ipAddressPrefixLength: Int? = null,
    @SerialName("IPv6Gateway") public val ipv6Gateway: String? = null,
    @SerialName("MacAddress") public val macAddress: String? = null,
    @SerialName("Ports") public val ports: Map<String, Map<String, String>> = emptyMap(),
    @SerialName("SandboxKey") public val sandboxKey: String,
    @SerialName("EndpointID") public val endpointId: String,
    @SerialName("Gateway") public val gateway: String,
    @SerialName("Networks") public val networks: List<EndpointSettings> = emptyList(),
)
