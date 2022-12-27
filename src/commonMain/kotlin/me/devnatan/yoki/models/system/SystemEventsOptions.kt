package me.devnatan.yoki.models.system

import kotlinx.serialization.Serializable

@Serializable
public data class SystemEventsOptions(
    public var since: String? = null,
    public var until: String? = null,
    public var filters: String? = null,
)
