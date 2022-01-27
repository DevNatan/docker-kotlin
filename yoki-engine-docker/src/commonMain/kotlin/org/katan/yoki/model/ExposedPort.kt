package org.katan.yoki.model

import kotlinx.serialization.Serializable

@Serializable
public data class ExposedPort internal constructor(
    public val protocol: String,
    public val port: Short
)

public fun exposedPort(port: Short): ExposedPort = ExposedPort(Tcp, port)
public fun exposedPort(port: Short, protocol: String): ExposedPort = ExposedPort(protocol, port)
