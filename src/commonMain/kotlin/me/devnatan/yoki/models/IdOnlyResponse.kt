package org.katan.yoki.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class IdOnlyResponse(
    @SerialName("Id") val id: String,
)
