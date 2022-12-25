package org.katan.yoki.yoki.models.system

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public typealias SystemEventType = String
public typealias SystemEventScope = String

@Serializable
public data class SystemEvent(
    @SerialName("Type") public val type: SystemEventType,
    @SerialName("Action") public val action: String,
    @SerialName("Actor") public val actor: String,
    @SerialName("scope") public val scope: String,
    @SerialName("time") public val time: Long,
    @SerialName("timeNano") public val timeInNanos: Long,
) {

    @Serializable
    public data class Actor(
        @SerialName("ID") public val id: String,
        @SerialName("Attributes") public val attributes: Map<String, String> = emptyMap(),
    )

    public companion object {

        public const val LocalScope: SystemEventScope = "local"
        public const val SwarmScope: SystemEventScope = "swarm"

        public const val BuilderType: SystemEventType = "builder"
        public const val ConfigType: SystemEventType = "config"
        public const val ContainerType: SystemEventType = "container"
        public const val DaemonType: SystemEventType = "daemon"
        public const val ImageType: SystemEventType = "image"
        public const val NetworkType: SystemEventType = "network"
        public const val NodeType: SystemEventType = "node"
        public const val PluginType: SystemEventType = "plugin"
        public const val SecretType: SystemEventType = "secret"
        public const val ServiceType: SystemEventType = "service"
        public const val VolumeType: SystemEventType = "volume"
    }
}
