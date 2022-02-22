package org.katan.yoki.model.secret

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Secrets are sensitive data that can be used by Docker services.
 *
 * @property id The id of the secret.
 * @property createdAt The instant this secret was created.
 * @property updatedAt The instant this secret was updated.
 */
@Serializable
public data class Secret(
    @SerialName("ID") public val id: String,
    @SerialName("CreatedAt") public val createdAt: Instant,
    @SerialName("UpdatedAt") public val updatedAt: Instant
    // TODO version
)

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
