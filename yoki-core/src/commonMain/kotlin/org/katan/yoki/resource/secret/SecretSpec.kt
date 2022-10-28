package org.katan.yoki.resource.secret

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Secret specification.
 *
 * @property name User-defined name of the secret.
 * @property driver Driver represents a driver (network, logging, secrets).
 * @property templating Driver represents a driver (network, logging, secrets).
 */
@Serializable
public data class SecretSpec(
    @SerialName("Name") public val name: String,
    @SerialName("Driver") public val driver: SecretSpecDriver,
    @SerialName("Templating") public val templating: SecretSpecDriver
)
