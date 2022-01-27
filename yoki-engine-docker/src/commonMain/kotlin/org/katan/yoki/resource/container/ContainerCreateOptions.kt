package org.katan.yoki.resource.container

import kotlinx.serialization.*
import org.katan.yoki.util.*
import kotlin.time.*

public data class ContainerCreateOptions(
    @SerialName("Hostname") public var hostName: String? = null,
    @SerialName("User") public var user: String? = null,
    @SerialName("Domainname") public var domainName: String? = null,
    @SerialName("AttachStdin") public var attachStdin: Boolean? = null,
    @SerialName("AttachStdout") public var attachStdout: Boolean? = null,
    @SerialName("AttachStderr") public var attachStderr: Boolean? = null,
    @SerialName("OpenStdin") public var openStdin: Boolean? = null,
    @SerialName("StdinOnce") public var onceStdin: Boolean? = null,
    // TODO exposed ports
    @SerialName("Tty") public var tty: Boolean = false,
    @SerialName("Env") public var env: Map<String, String>? = null,
    @SerialName("Cmd") public var command: String? = null,
    @SerialName("ArgsEscaped") public var escapedArgs: Boolean? = null,
    @SerialName("Image") public var image: String? = null,
    @SerialName("WorkingDir") public var workingDirectory: String? = null,
    @SerialName("Entrypoint") public var entrypoint: List<String>? = null,
    @SerialName("NetworkDisabled") public var disableNetwork: Boolean? = null,
    @SerialName("MacAddress") public var macAddress: String? = null,
    @SerialName("StopSignal") public var stopSignal: String? = null,
    @SerialName("StopTimeout") public var stopTimeout: Int? = null,
    @SerialName("Shell") public var shell: List<String>? = null,
    @SerialName("Labels") public var labels: Map<String, String>? = null,
    @SerialName("OnBuild") public var buildMetadata: List<String>? = null,
    @SerialName("ExposedPorts") public var exposedPorts: Map<String, @Contextual Any>? = null
) : Options()

public fun ContainerCreateOptions.stopTimeout(stopTimeout: Duration) {
    this.stopTimeout = stopTimeout.inWholeSeconds.toInt()
}

