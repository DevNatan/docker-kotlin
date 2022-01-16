package org.katan.yoki.engine.docker.model.container

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author João Victor Gomides Cruz
 */
@Serializable
public data class NetworkSettings(
    @SerialName("Networks") public val networks: List<Network>?
) {

    @Serializable
    public data class Network(
        @SerialName("IPAMConfig") public val ipamConfig: IPAMConfig?,
        @SerialName("Links") public val links: List<String>?,
        @SerialName("Aliases") public val aliases: List<String>?,
        @SerialName("NetworkID") public val networkId: String,
        @SerialName("EndpointID") public val endpointId: String,
        @SerialName("Gateway") public val gateway: String,
        @SerialName("IPAddress") public val ipAddress: String,
        @SerialName("IPPrefixLen") public val ipPrefixLen: Int,
        @SerialName("IPv6Gateway") public val ipv6Gateway: String,
        @SerialName("GlobalIPv6Address") public val globalIpv6Address: String,
        @SerialName("GlobalIPv6PrefixLen") public val globalIpv6PrefixLen: Long,
        @SerialName("MacAddress") public val macAddress: String,
        @SerialName("DriverOpts") public val driverOpts: Map<String, String>?,
    ) {

        @Serializable
        public data class IPAMConfig(
            @SerialName("IPv4Address") public val ipv4Address: String,
            @SerialName("IPv6Address") public val ipv6Address: String,
            @SerialName("LinkLocalIPs") public val linkLocalIps: List<String>?,
        )

    }

}
