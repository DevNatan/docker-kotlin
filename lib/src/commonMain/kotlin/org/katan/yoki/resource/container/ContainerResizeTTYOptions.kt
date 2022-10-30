package org.katan.yoki.resource.container

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * TTY resize container options.
 *
 * @property width Width of the TTY session in characters.
 * @property height Height of the TTY session in characters.
 */
@Serializable
public data class ContainerResizeTTYOptions(
    @SerialName("w") public var width: Int? = null,
    @SerialName("h") public var height: Int? = null,
)
