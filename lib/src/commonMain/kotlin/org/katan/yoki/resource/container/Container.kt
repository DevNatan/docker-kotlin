package org.katan.yoki.resource.container

import kotlinx.serialization.Serializable

@Serializable
public data class Container(
    val id: String,
    val image: String,
    val name: String? = null,
)
