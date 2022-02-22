package org.katan.yoki.resource.secret

import org.katan.yoki.*
import org.katan.yoki.model.secret.*
import io.ktor.client.request.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/**
 * Secrets are sensitive data that can be used by Docker services.
 * Swarm mode must be enabled for these endpoints to work.
 */
public class SecretResource(private val engine: DockerEngine) {

    private companion object {
        const val BASE_PATH = "/secrets"
    }

    /**
     * Lists all secrets.
     * @param filters Filters to process on the secrets list.
     */
    public suspend fun list(filters: SecretListFilters? = null): List<Secret> {
        return engine.httpClient.get(BASE_PATH) {
            filters?.let { parameter("filters", Json.Default.encodeToString(filters)) }
        }
    }

    /**
     * Inspects a secret.
     *
     * @param id The id of the secret.
     */
    public suspend fun get(id: String): Secret {
        return engine.httpClient.get("$BASE_PATH/$id")
    }

    /**
     * Deletes a secret.
     *
     * @param id The id of the secret.
     */
    public suspend fun delete(id: String) {
        engine.httpClient.delete<Unit>("$BASE_PATH/$id")
    }
}

/**
 * Lists all secrets.
 * @param filters Filters to process on the secrets list.
 */
public suspend inline fun SecretResource.list(filters: SecretListFilters.() -> Unit): List<Secret> {
    return list(SecretListFilters().apply(filters))
}

/**
 * Filters to apply to the secrets list process.
 *
 * @property id The secret id.
 * @property name The secret name.
 * @property label Secret labels.
 */
@Serializable
public data class SecretListFilters(
    public var id: String? = null,
    public var label: String? = null,
    public var name: String? = null
)

/**
 * Sets the secret list filters to the given label pair.
 * @param label The label pair (key, value).
 */
public fun SecretListFilters.label(label: Pair<String, String>) {
    this.label = "${label.first}=${label.second}"
}
