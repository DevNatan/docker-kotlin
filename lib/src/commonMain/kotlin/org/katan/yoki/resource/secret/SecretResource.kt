package org.katan.yoki.resource.secret

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Secrets are sensitive data that can be used by Docker services.
 * Swarm mode must be enabled for these endpoints to work.
 */
public class SecretResource(
    private val httpClient: HttpClient,
    private val json: Json,
) {

    private companion object {
        const val BASE_PATH = "/secrets"
    }

    /**
     * Lists all secrets.
     * @param filters Filters to process on the secrets list.
     */
    public suspend fun list(filters: SecretListFilters? = null): List<Secret> {
        return httpClient.get(BASE_PATH) {
            parameter("filters", filters?.let(json::encodeToString))
        }
    }

    /**
     * Inspects a secret.
     *
     * @param id The id of the secret.
     */
    public suspend fun get(id: String): Secret {
        return httpClient.get("$BASE_PATH/$id")
    }

    /**
     * Deletes a secret.
     *
     * @param id The id of the secret.
     */
    public suspend fun delete(id: String) {
        httpClient.delete<Unit>("$BASE_PATH/$id")
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
 * Sets the secret list filters to the given label pair.
 * @param label The label pair (key, value).
 */
public fun SecretListFilters.label(label: Pair<String, String>) {
    this.label = "${label.first}=${label.second}"
}
