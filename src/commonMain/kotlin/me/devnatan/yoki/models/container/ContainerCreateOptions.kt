package me.devnatan.yoki.models.container

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.devnatan.yoki.models.HealthConfig
import me.devnatan.yoki.models.HostConfig
import me.devnatan.yoki.models.network.NetworkingConfig
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Serializable
public data class ContainerCreateOptions(
    public var name: String? = null,
    @SerialName("Hostname") public var hostName: String? = null,
    @SerialName("Domainname") public var domainName: String? = null,
    @SerialName("User") public var user: String? = null,
    @SerialName("AttachStdin") public var attachStdin: Boolean? = null,
    @SerialName("Cmd") public var command: List<String>? = null,
    @SerialName("Healthcheck") public var healthcheck: HealthConfig? = null,
    @SerialName("ArgsEscaped") public var escapedArgs: Boolean? = null,
    @SerialName("Image") public var image: String? = null,
    @SerialName("Volumes") public var volumes: Map<String, @Contextual Any>? = null,
    @SerialName("WorkingDir") public var workingDirectory: String? = null,
    @SerialName("Entrypoint") public var entrypoint: List<String>? = null,
    @SerialName("NetworkDisabled") public var disabledNetwork: Boolean? = null,
    @SerialName("MacAddress") public var macAddress: String? = null,
    @SerialName("OnBuild") public var buildMetadata: List<String>? = null,
    @SerialName("Labels") public var labels: Map<String, String>? = null,
    @SerialName("StopSignal") public var stopSignal: String? = null,
    @SerialName("StopTimeout") public var stopTimeout: Int? = null,
    @SerialName("Shell") public var shell: List<String>? = null,
    @SerialName("HostConfig") public var hostConfig: HostConfig? = null,
    @SerialName("NetworkingConfig") public var networkingConfig: NetworkingConfig? = null,
)

public fun ContainerCreateOptions.healthcheck(block: HealthConfig.() -> Unit) {
    this.healthcheck = HealthConfig().apply(block)
}

public fun ContainerCreateOptions.hostConfig(block: HostConfig.() -> Unit) {
    this.hostConfig = HostConfig().apply(block)
}

public fun ContainerCreateOptions.volume(string: String) {
    this.volumes = mapOf(string to emptyMap<String, Any>()) + volumes.orEmpty()
}

public fun ContainerCreateOptions.networkingConfig(block: NetworkingConfig.() -> Unit) {
    this.networkingConfig = NetworkingConfig().apply(block)
}

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public var ContainerCreateOptions.stopTimeout: Duration?
    get() = stopTimeout?.toDuration(DurationUnit.NANOSECONDS)
    set(value) {
        stopTimeout = value?.inWholeNanoseconds?.toInt()
    }
