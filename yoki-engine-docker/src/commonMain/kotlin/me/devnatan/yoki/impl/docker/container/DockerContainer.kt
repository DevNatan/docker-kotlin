package me.devnatan.yoki.impl.docker.container

import kotlinx.serialization.Serializable
import me.devnatan.yoki.api.external.model.container.Container

/**
 * @see Container
 */
@Serializable
public data class DockerContainer(
    val image: String,
    val name: String? = null,
) : Container