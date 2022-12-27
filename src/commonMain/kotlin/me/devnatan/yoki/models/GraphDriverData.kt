package me.devnatan.yoki.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GraphDriverData(
    @SerialName("Name") public val name: String,
    @SerialName("Data") public val data: Map<String, String>,
)
