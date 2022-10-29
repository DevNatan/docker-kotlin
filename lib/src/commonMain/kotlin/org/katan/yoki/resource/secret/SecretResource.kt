package org.katan.yoki.resource.secret

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.katan.yoki.UnhandledYokiResourceException
import org.katan.yoki.io.requestCatching
import org.katan.yoki.io.throwResourceException
import org.katan.yoki.resource.IdOnlyResponse
import org.katan.yoki.resource.swarm.NodeNotPartOfSwarmException

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
    public suspend fun inspect(id: String): Secret {
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

    /**
     * Creates a new secret on the Docker swarm.
     *
     * @param options The secret spec.
     */
    public suspend fun create(options: SecretSpec): String {
        return httpClient.requestCatching(
            {
                post<IdOnlyResponse>("$BASE_PATH/create") {
                    body = options
                }
            },
            {
                val props = mapOf("secretName" to options.name)
                when (response.status) {
                    HttpStatusCode.Conflict -> throwResourceException(::SecretNameConflictException, props)
                    HttpStatusCode.ServiceUnavailable -> throwResourceException(::NodeNotPartOfSwarmException, props)
                    else -> throwResourceException(::UnhandledYokiResourceException, props)
                }
            },
        ).getOrThrow().id
    }

    /**
     * Updates an existing secret on the Docker swarm.
     *
     * @param id The secret id.
     * @param version The secret version.
     * @param options The new secret spec.
     */
    public suspend fun update(id: String, version: Long, options: SecretSpec) {
        httpClient.requestCatching(
            {
                post<Unit>("$BASE_PATH/$id/update") {
                    parameter("version", version)
                    body = options
                }
            },
            {
                val props = mapOf("id" to id, "version" to version)
                when (response.status) {
                    HttpStatusCode.NotFound -> throwResourceException(::SecretNotFoundException, props)
                    HttpStatusCode.ServiceUnavailable -> throwResourceException(::NodeNotPartOfSwarmException, props)
                    else -> throwResourceException(::UnhandledYokiResourceException, props)
                }
            },
        )
    }
}

/**
 * Lists all secrets.
 *
 * @param filters Filters to process on the secrets list.
 */
public suspend inline fun SecretResource.list(filters: SecretListFilters.() -> Unit): List<Secret> {
    return list(SecretListFilters().apply(filters))
}

public suspend inline fun SecretResource.create(options: SecretSpec.() -> Unit): String {
    return create(SecretSpec().apply(options))
}

public suspend inline fun SecretResource.update(id: String, version: Long, options: SecretSpec.() -> Unit) {
    return update(id, version, SecretSpec().apply(options))
}

/**
 * Sets the secret list filters to the given label pair.
 *
 * @param label The label pair (key, value).
 */
public fun SecretListFilters.label(label: Pair<String, String>) {
    this.label = "${label.first}=${label.second}"
}
