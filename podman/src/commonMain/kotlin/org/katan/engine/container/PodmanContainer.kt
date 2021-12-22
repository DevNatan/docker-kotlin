package org.katan.engine.container

import kotlinx.serialization.Serializable

/**
 * @see Container
 */
@Serializable
data class PodmanContainer(
    val name: String
) : Container