package org.katan.yoki.engine.docker.container

import kotlinx.serialization.Serializable
import org.katan.yoki.api.external.model.container.Container

/**
 * @see Container
 */
@Serializable
public data class DockerContainer(
    val image: String,
    val name: String? = null,
) : Container