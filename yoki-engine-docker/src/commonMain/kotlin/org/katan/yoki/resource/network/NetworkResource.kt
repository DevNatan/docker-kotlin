package org.katan.yoki.resource.network

import org.katan.yoki.*
import org.katan.yoki.model.network.*
import org.katan.yoki.util.*
import kotlin.jvm.*
import io.ktor.client.request.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/**
 * Networks are user-defined networks that containers can be attached to. See the [networking documentation](https://docs.docker.com/network/)
 * for more information.
 */
public class NetworkResource(private val engine: DockerEngine) {

    private companion object {

        const val BASE_PATH = "/networks"

        const val LIST_FILTERS = "filters"
        const val INSPECT_VERBOSE = "verbose"
        const val INSPECT_SCOPE = "scope"
        const val CONNECT_CONTAINER_TO_NETWORK_CONTAINER = "Container"
        const val DISCONNECT_CONTAINER_TO_NETWORK_CONTAINER = "Container"
        const val PRUNE_FILTERS = "filters"
    }

    /**
     * Returns a list of networks.
     *
     * @param filters Filters to to process on the networks list.
     * @see <a href="https://docs.docker.com/engine/api/latest/#operation/NetworkList">NetworkList</a>
     */
    public suspend fun list(filters: NetworksFilters? = null): List<Network> {
        return engine.httpClient.get(BASE_PATH) {
            filters?.let { parameter(LIST_FILTERS, Json.Default.encodeToString(it)) }
        }
    }

    /**
     * Inspects a network.
     *
     * @param id The network id or name.
     * @param options The network inspection options.
     * @see <a href="https://docs.docker.com/engine/api/latest/#operation/NetworkInspect">NetworkInspect</a>
     */
    public suspend fun inspect(id: String, options: NetworkInspect? = null): Network {
        return engine.httpClient.get("$BASE_PATH/$id") {
            options?.verbose?.let { parameter(INSPECT_VERBOSE, it) }
            options?.scope?.let { parameter(INSPECT_SCOPE, it) }
        }
    }

    /**
     * Removes a network.
     *
     * @param id The network id or name.
     * @see <a href="https://docs.docker.com/engine/api/latest/#operation/NetworkDelete">NetworkDelete</a>
     */
    public suspend fun remove(id: String) {
        engine.httpClient.delete<Unit>("$BASE_PATH/$id")
    }

    /**
     * Creates a new network.
     *
     * @param config The network configuration.
     * @see <a href="https://docs.docker.com/engine/api/latest/#operation/NetworkCreate">NetworkCreate</a>
     */
    public suspend fun create(config: NetworkConfig): Network {
        checkNotNull(config.name) { "Network name is required and cannot be null" }

        return engine.httpClient.post("$BASE_PATH/create") {
            body = config
        }
    }

    /**
     * Deletes all unused networks.
     *
     * @param options The network prune options.
     *
     * @see <a href="https://docs.docker.com/engine/api/latest/#operation/NetworkPrune">NetworkPrune</a>
     */
    public suspend fun prune(options: NetworkPrune? = null) {
        engine.httpClient.post<Unit>("$BASE_PATH/prune") {
            parameter(PRUNE_FILTERS, options)
        }
    }

    /**
     * Connects a container to a network.
     *
     * @param id The network id or name.
     * @param container The id or name of the container to connect to the network.
     * @see <a href="https://docs.docker.com/engine/api/v1.41/#operation/NetworkConnect">NetworkConnect</a>
     */
    public suspend fun connectContainer(id: String, container: String) {
        engine.httpClient.post<Unit>("$BASE_PATH/$id/connect") {
            body = mapOf(CONNECT_CONTAINER_TO_NETWORK_CONTAINER to container)
        }
    }

    /**
     * Disconnects a container to a network.
     *
     * @param id The network id or name.
     * @param container The id or name of the container to connect to the network.
     * @see <a href="https://docs.docker.com/engine/api/latest/#operation/NetworkDisconnect">NetworkDisconnect</a>
     */
    public suspend fun disconnectContainer(id: String, container: String) {
        engine.httpClient.post<Unit>("$BASE_PATH/$id/disconnect") {
            body = mapOf(DISCONNECT_CONTAINER_TO_NETWORK_CONTAINER to container)
        }
    }
}

/**
 * Creates a new network.
 *
 * @param config The network configuration.
 * @see <a href="https://docs.docker.com/engine/api/latest/#operation/NetworkCreate">NetworkCreate</a>
 */
public suspend inline fun NetworkResource.create(config: NetworkConfig.() -> Unit): Network {
    return create(NetworkConfig().apply(config))
}

/**
 * Returns a list of networks.
 *
 * @param filters Filters to to process on the networks list.
 * @see <a href="https://docs.docker.com/engine/api/latest/#operation/NetworkList">NetworkList</a>
 */
public suspend inline fun NetworkResource.list(filters: NetworksFilters.() -> Unit): List<Network> {
    return list(NetworksFilters().apply(filters))
}

/**
 * Inspects a network.
 *
 * @param id The network id or name.
 * @param options The network inspection options.
 * @see <a href="https://docs.docker.com/engine/api/latest/#operation/NetworkInspect">NetworkInspect</a>
 */
public suspend inline fun NetworkResource.inspect(id: String, options: NetworkInspect.() -> Unit): Network {
    return inspect(id, NetworkInspect().apply(options))
}

/**
 * Deletes all unused networks.
 *
 * @param options The network prune options.
 *
 * @see <a href="https://docs.docker.com/engine/api/latest/#operation/NetworkPrune">NetworkPrune</a>
 */
public suspend inline fun NetworkResource.prune(options: NetworkPrune.() -> Unit) {
    prune(NetworkPrune().apply(options))
}

/**
 * Network list filters.
 *
 * @property dangling When set to `true`, returns all networks that are not in use by a container.
 * When set to `false`, only networks that are in use by one or more containers are returned.
 * @property driver Matches a network's driver.
 * @property id Matches all or part of a network ID.
 * @property name Matches all or part of a network name.
 * @property scope Filters networks by scope.
 * @property type Filters networks by type. The `custom` type returns all user-defined networks.
 * @see NetworkResource.list
 */
@Serializable
public data class NetworksFilters(
    public var dangling: Boolean = false,
    public var driver: String? = null,
    public var id: String? = null,
    public var name: String? = null,
    public var scope: String? = null,
    public var type: String? = null
)

/**
 * Network inspect options
 *
 * @property verbose Detailed inspect output for troubleshooting (default: false).
 * @property scope Filter the network by scope (swarm, global, or local).
 * @see NetworkLocalScope
 * @see NetworkGlobalScope
 * @see NetworkSwarmScope
 * @see NetworkResource.inspect
 */
@Serializable
public data class NetworkInspect(
    public var verbose: Boolean? = null,
    public var scope: String? = null
) {

    init {
        scope?.let { requireNetworkScope(it) }
    }
}

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
public data class NetworkConfig(
    @SerialName("Name") public var name: String? = null,
    @SerialName("CheckDuplicate") public var checkDuplicate: Boolean? = null,
    @SerialName("Driver") public var driver: String? = null,
    @SerialName("Internal") public var isInternal: Boolean? = null,
    @SerialName("Attachable") public var isAttachable: Boolean? = null,
    @SerialName("Ingress") public var ingress: Boolean? = null,
    @SerialName("IPAM") public var ipam: IPAM? = null,
    @SerialName("EnableIPV6") public var enableIpv6: Boolean? = null,
    @SerialName("Options") public var options: Map<String, String>? = null,
    @SerialName("Labels") public var labels: Map<String, String>? = null
)

/**
 * Network prune options.
 *
 * @property until Prune networks created before this timestamp.
 * The timestamp can be Unix timestamps, date formatted timestamps,
 * or Go duration strings (e.g. `10m`, `1h30m`) computed relative to the daemon machine’s time.
 * @property label Prune networks with (or without, in case `label!=...` is used) the specified labels.
 * @see NetworkResource.prune
 */
@Serializable
public data class NetworkPrune(
    public var until: String? = null,
    public var label: String? = null
)
