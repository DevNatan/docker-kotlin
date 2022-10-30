package org.katan.yoki.resource.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network configuration.
 *
 * @property name The network's name.
 * @property checkDuplicate Check for networks with duplicate names.
 * Since Network is primarily keyed based on a random ID and not on the name,
 * and network name is strictly a user-friendly alias to the network which is uniquely identified using ID,
 * there is no guaranteed way to check for duplicates. CheckDuplicate is there to provide a best effort checking
 * of any networks which has the same name but it is not guaranteed to catch all name collisions.
 * @property driver Name of the network driver plugin to use.
 * @property isInternal Restrict external access to the network.
 * @property isAttachable Globally scoped network is manually attachable by regular containers from workers in swarm mode.
 * @property ingress Ingress network is the network which provides the routing-mesh in swarm mode.
 * @property ipam IPAM configuration.
 * @property enableIpv6 Enable IPv6 on the network.
 * @property options Network specific options to be used by the drivers.
 * @property labels User-defined key/value metadata.
 * @see NetworkResource.create
 */
@Serializable
public data class NetworkCreateConfig @JvmOverloads public constructor(
    @SerialName("Name") public var name: String? = null,
    @SerialName("CheckDuplicate") public var checkDuplicate: Boolean? = null,
    @SerialName("Driver") public var driver: String? = null,
    @SerialName("Internal") public var isInternal: Boolean? = null,
    @SerialName("Attachable") public var isAttachable: Boolean? = null,
    @SerialName("Ingress") public var ingress: Boolean? = null,
    @SerialName("IPAM") public var ipam: IPAM? = null,
    @SerialName("EnableIPV6") public var enableIpv6: Boolean? = null,
    @SerialName("Options") public var options: Map<String, String>? = null,
    @SerialName("Labels") public var labels: Map<String, String>? = null,
)
