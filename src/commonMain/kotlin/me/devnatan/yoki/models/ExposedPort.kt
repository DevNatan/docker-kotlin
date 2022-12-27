package me.devnatan.yoki.models

import kotlinx.serialization.Serializable

@Serializable
public data class ExposedPort internal constructor(
    public val protocol: String,
    public val port: Short,
) {

    public companion object {

        public const val TCP: String = "tcp"
        public const val UDP: String = "udp"
        public const val SCTP: String = "stcp"
    }
}

public fun exposedPort(port: Short): ExposedPort = ExposedPort(ExposedPort.TCP, port)
public fun exposedPort(port: Short, protocol: String): ExposedPort = ExposedPort(protocol, port)
