package org.katan.yoki.models.container

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.katan.yoki.models.UserDefinedMetadata
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
    @SerialName("AttachStdout") public var attachStdout: Boolean? = null,
    @SerialName("AttachStderr") public var attachStderr: Boolean? = null,
    @SerialName("ExposedPorts") public var exposedPorts: Map<String, @Contextual Any>? = null,
    @SerialName("Tty") public var tty: Boolean = false,
    @SerialName("OpenStdin") public var openStdin: Boolean? = null,
    @SerialName("StdinOnce") public var onceStdin: Boolean? = null,
    @SerialName("Env") public var env: List<String>? = null,
    @SerialName("Cmd") public var command: List<String>? = null,
    @SerialName("Healthcheck") public var healthcheck: HealthConfig,
    @SerialName("ArgsEscaped") public var escapedArgs: Boolean? = null,
    @SerialName("Image") public var image: String? = null,
    // TODO volumes
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
    // TODO networking config
) {

    public var onBuild: List<String>?
        get() = buildMetadata
        set(value) {
            buildMetadata = value
        }

    @Serializable
    public data class HealthConfig(
        @SerialName("Test") public var test: List<String>? = null,
        @SerialName("Interval") public var interval: Int? = null,
        @SerialName("Timeout") public var timeout: Int? = null,
        @SerialName("Retries") public var retries: Int? = null,
        @SerialName("StartPeriod") public var startPeriod: Int? = null,
    )

    @Serializable
    public data class HostConfig(
        @SerialName("CpuShares") public var cpuShares: Int? = null,
        @SerialName("Memory") public var memory: Long? = null,
        @SerialName("CgroupParent") public var cgroupParent: String? = null,
        @SerialName("BlkioWeight") public var blkioWeight: Int? = null,
        @SerialName("BlkioWeightDevice") public var blkioWeightDevice: List<BlkioWeightDevice>? = null,
        @SerialName("BlkioDeviceReadBps") public var blkioDeviceReadBps: List<ThrottleDevice>? = null,
        @SerialName("BlkioDeviceWriteBps") public var blkioDeviceWriteBps: List<ThrottleDevice>? = null,
        @SerialName("BlkioDeviceReadIOps") public var blkioDeviceReadIOps: List<ThrottleDevice>? = null,
        @SerialName("BlkioDeviceWriteIOps") public var blkioDeviceWriteIOps: List<ThrottleDevice>? = null,
        @SerialName("CpuPeriod") public var cpuPeriod: Long? = null,
        @SerialName("CpuQuota") public var cpuQuota: Long? = null,
        @SerialName("CpuRealtimePeriod") public var cpuRealtimePeriod: Long? = null,
        @SerialName("CpuRealtimeRuntime") public var cpuRealtimeRuntime: Long? = null,
        @SerialName("CpusetCpus") public var allowedCpus: String? = null,
        @SerialName("CpusetMems") public var allowedMEMs: String? = null,
        @SerialName("Devices") public var devices: List<DeviceMapping>? = null,
        @SerialName("DeviceCgroupRules") public var deviceCgroupRules: List<String>? = null,
        @SerialName("DeviceRequests") public var deviceRequests: List<DeviceRequest>? = null,
        @SerialName("KernelMemory") public var kernelMemory: Long? = null,
        @SerialName("KernelMemoryTCP") public var kernelMemoryTcp: Long? = null,
        @SerialName("MemoryReservation") public var memoryReservation: Long? = null,
        @SerialName("MemorySwap") public var memorySwap: Long? = null,
        @SerialName("MemorySwappiness") public var memorySwappiness: Long? = null,
        @SerialName("NanoCpus") public var nanoCpus: Long? = null,
        @SerialName("OomKillDisable") public var disabledOOMKiller: Boolean? = null,
        @SerialName("Init") public var init: Boolean? = null,
        @SerialName("PidsLimit") public var pidsLimit: Long? = null,
        @SerialName("Ulimits") public var resourcesLimit: List<ResourceLimit>? = null,
        @SerialName("CpuCount") public var cpuCount: Long? = null,
        @SerialName("CpuPercent") public var cpuPercent: Long? = null,
        @SerialName("IOMaximumIOps") public var IOMaximumIOps: Long? = null,
        @SerialName("IOMaximumBandwidth") public var IOMaximumBandwidth: Long? = null,
        // TODO provide better way to apply binds
        @SerialName("Binds") public var binds: List<String>? = null,
        @SerialName("ContainerIDFile") public var containerIDFile: String? = null,
        @SerialName("LogConfig") public var logConfig: LogConfig,
        @SerialName("NetworkMode") public var networkMode: String,
        @SerialName("PortBindings") public var portBindings: Map<String, List<PortBinding>?>? = null,
        @SerialName("RestartPolicy") public var restartPolicy: RestartPolicy,
        @SerialName("AutoRemove") public var autoRemove: Boolean? = null,
        @SerialName("VolumeDriver") public var volumeDriver: String? = null,
        // TODO provide a better way to apply volumes
        @SerialName("VolumesFrom") public var volumesFrom: String? = null,
        // TODO mounts
        @SerialName("CapAdd") public var capAdd: List<String>? = null,
        @SerialName("CapDrop") public var capDrop: List<String>? = null,
        // TODO provide constants for possible cgroupns mode values
        @SerialName("CgroupnsMode") public var cgroupnsMode: String? = null,
        @SerialName("Dns") public var dnsServers: List<String>? = null,
        @SerialName("DnsOptions") public var dnsOptions: List<String>? = null,
        @SerialName("DnsSearch") public var dnsSearch: List<String>? = null,
        // TODO allow use pairs to apply extra hosts
        @SerialName("ExtraHosts") public var extraHosts: List<String>? = null,
        @SerialName("GroupAdd") public var groupAdd: List<String>? = null,
        // TODO provide constants for possible ipc mode values
        @SerialName("IpcMode") public var ipcMode: String? = null,
        @SerialName("Cgroup") public var cgroup: String? = null,
        @SerialName("Links") public var links: List<String>? = null,
        @SerialName("OomScoreAdj") public var oomScoreAdj: Int? = null,
        // TODO provide a batter way to apply pid mode
        @SerialName("PidMode") public var pidMode: String? = null,
        @SerialName("Privileged") public var privileged: Boolean? = null,
        @SerialName("PublishAllPorts") public var publishAllPorts: Boolean? = null,
        @SerialName("ReadonlyRootFs") public var readonlyRootFs: Boolean? = null,
        @SerialName("SecurityOpt") public var securityOpt: List<String>? = null,
        @SerialName("StorageOpt") public var storageOpt: UserDefinedMetadata? = null,
        @SerialName("Tmpfs") public var tmpfs: UserDefinedMetadata? = null,
        @SerialName("UTSMode") public var utsMode: String? = null,
        @SerialName("UsernsMode") public var userNamespaceMode: String? = null,
        @SerialName("ShmSize") public var shmSize: Int? = null,
        @SerialName("Sysctls") public var sysctls: UserDefinedMetadata? = null,
        @SerialName("Runtime") public var runtime: String? = null,
        @SerialName("ConsoleSize") public var consoleSize: IntArray,
        // TODO provide constants for possible isolation values
        @SerialName("Isolation") public var isolation: String? = null,
        @SerialName("MaskedPaths") public var maskedPaths: List<String>? = null,
        @SerialName("ReadonlyPaths") public var readonlyPaths: List<String>? = null,
        )

}

@Serializable
public data class BlkioWeightDevice(
    @SerialName("Path") public var path: String,
    @SerialName("Weight") public var weight: Int,
)

@Serializable
public data class ThrottleDevice(
    @SerialName("Path") public var path: String,
    // TODO check if it's really int64
    @SerialName("Rate") public var rate: Long,
)

@Serializable
public data class DeviceMapping(
    @SerialName("PathOnHost") val pathOnHost: String? = null,
    @SerialName("PathInContainer") val pathInContainer: String? = null,
    @SerialName("CgroupPermissions") val cgroupPermissions: String? = null,
)

@Serializable
public data class DeviceRequest(
    @SerialName("Driver") public var driver: String? = null,
    @SerialName("Count") public var count: Int? = null,
    @SerialName("DeviceIDs") public var deviceIDs: List<String>? = null,
    @SerialName("Capabilities") public var capabilities: List<String>? = null,
    @SerialName("Options") public var options: UserDefinedMetadata? = null,
)

@Serializable
public data class ResourceLimit(
    @SerialName("Name") public var name: String,
    @SerialName("Soft") public var soft: Int,
    @SerialName("Hard") public var hard: Int,
)

@Serializable
public data class LogConfig(
    @SerialName("Type") public var type: String,
    @SerialName("Config") public var config: Map<String, String>? = null,
)

@Serializable
public data class PortBinding(
    @SerialName("HostIp") public var ip: String? = null,
    @SerialName("HostPort") public var port: String? = null,
)

@Serializable
public data class RestartPolicy(
    @SerialName("Name") public var name: String,
    @SerialName("MaximumRetryCount") public var maximumRetryCount: Int,
) {

    public companion object {
        public const val DoNotRestart: String = ""
        public const val AlwaysRestart: String = "always"
        public const val RestartUnlessStopped: String = "unless-stopped"
        public const val RestartOnFailure: String = "on-failure"
    }

}

@JvmSynthetic
public fun ContainerCreateOptions.healthcheck(block: ContainerCreateOptions.HealthConfig.() -> Unit) {
    this.healthcheck = ContainerCreateOptions.HealthConfig().apply(block)
}

public fun ContainerCreateOptions.stopTimeout(stopTimeout: Duration) {
    this.stopTimeout = stopTimeout.inWholeSeconds.toInt()
}

public fun ContainerCreateOptions.env(environmentValues: Map<String, String?>) {
    this.env = environmentValues.entries.map { (k, v) -> "$k=$v" }
}

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public var ContainerCreateOptions.HealthConfig.interval: Duration?
    get() = interval?.toDuration(DurationUnit.NANOSECONDS)
    set(value) {
        interval = value?.inWholeNanoseconds?.toInt()
    }

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public var ContainerCreateOptions.HealthConfig.timeout: Duration?
    get() = timeout?.toDuration(DurationUnit.NANOSECONDS)
    set(value) {
        timeout = value?.inWholeNanoseconds?.toInt()
    }

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public var ContainerCreateOptions.HealthConfig.startPeriod: Duration?
    get() = startPeriod?.toDuration(DurationUnit.NANOSECONDS)
    set(value) {
        startPeriod = value?.inWholeNanoseconds?.toInt()
    }