package org.katan.yoki.engine.docker.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Port(
    @SerialName("PrivatePort") public val privatePort: Short,
    @SerialName("PublicPort") public val publicPort: Short,
    @SerialName("Type") public val type: String
)
