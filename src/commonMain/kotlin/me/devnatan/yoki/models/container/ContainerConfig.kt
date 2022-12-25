package me.devnatan.yoki.models.container

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.devnatan.yoki.models.HealthConfig

@Serializable
public data class ContainerConfig(
    @SerialName("Hostname") public val hostname: String? = null,
    @SerialName("Domainname") public val domainname: String? = null,
    @SerialName("User") public val user: String? = null,
    @SerialName("AttachStdin") public val attachStdin: Boolean? = null,
    @SerialName("AttachStdout") public val attachStdout: Boolean? = null,
    @SerialName("AttachStderr") public val attachStderr: Boolean? = null,
    @SerialName("ExposedPorts") public val exposedPorts: Map<String, String> = emptyMap(),
    @SerialName("Tty") public val tty: Boolean? = null,
    @SerialName("OpenStdin") public val openStdin: Boolean? = null,
    @SerialName("StdinOnce") public val stdinOnce: Boolean? = null,
    @SerialName("Env") public val env: List<String> = emptyList(),
    @SerialName("Cmd") public val command: List<String> = emptyList(),
    @SerialName("Healthcheck") public val healthcheck: HealthConfig? = null,
    @SerialName("ArgsEscaped") public val argsEscaped: Boolean? = null,
    @SerialName("Image") public val image: String? = null,
    @SerialName("Volumes") public val volumes: Map<String, String> = emptyMap(),
    @SerialName("WorkingDir") public val workingDir: String? = null,
    @SerialName("Entrypoint") public val entrypoint: List<String> = emptyList(),
    @SerialName("NetworkDisabled") public val networkDisabled: Boolean? = null,
    @SerialName("MacAddress") public val macAddress: String? = null,
    @SerialName("OnBuild") public val onBuild: List<String> = emptyList(),
    @SerialName("Labels") public val labels: Map<String, String> = emptyMap(),
    @SerialName("StopSignal") public val stopSignal: String? = null,
    @SerialName("StopTimeout") public val stopTimeout: Int? = null,
    @SerialName("Shell") public val shell: List<String> = emptyList(),
)
