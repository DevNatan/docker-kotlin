package org.katan.yoki.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal value class IdOnlyResponse(
    @SerialName("Id") val id: String,
)
