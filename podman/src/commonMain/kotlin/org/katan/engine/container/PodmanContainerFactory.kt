package org.katan.engine.container

import io.ktor.client.*
import io.ktor.client.request.*

/**
 * @see ContainerFactory
 */
class PodmanContainerFactory(val httpClient: HttpClient) : ContainerFactory {

    /**
     * Creates a new container.
     *
     * @see <a href="https://docs.podman.io/en/latest/_static/api.html#operation/ContainerCreateLibpod">containers/create</a>
     */
    override suspend fun create(container: Container): String {
        check(container is PodmanContainer)

        return httpClient.post<Map<String, Any>>("/containers/create") {
            body = container
        }["Id"] as String
    }

}