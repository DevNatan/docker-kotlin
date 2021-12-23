package org.katan.engine.container

import kotlinx.serialization.Serializable

/**
 * @see Container
 */
@Serializable
data class PodmanContainer(
    val image: String,
    val name: String? = null,
) : Container