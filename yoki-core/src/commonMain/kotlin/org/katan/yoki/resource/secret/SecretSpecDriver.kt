package org.katan.yoki.resource.secret

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Secret specification driver details.
 *
 * @property name The name of the driver.
 * @property options Driver-specific options.
 */
@Serializable
public data class SecretSpecDriver(
    @SerialName("Name") public val name: String,
    @SerialName("Options") public val options: Map<String, String>
)
