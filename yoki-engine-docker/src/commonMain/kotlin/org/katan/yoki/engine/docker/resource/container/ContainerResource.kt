package org.katan.yoki.engine.docker.resource.container

import io.ktor.client.request.*
import io.ktor.http.*
import org.katan.yoki.engine.docker.*
import org.katan.yoki.engine.docker.model.container.*

/**
 * @see ContainerResource
 */
public class ContainerResource(private val engine: DockerEngine) {

    /**
     * Creates a new container.
     *
     * @see <a href="https://docs.podman.io/en/latest/_static/api.html#operation/ContainerCreateLibpod">ContainerCreateLibpod</a>
     */
    public suspend fun create(options: Map<String, Any>): String {
        require(ContainerImage in options) { "Container Image is required" }

        return engine.httpClient.post<Map<String, Any>>("/containers/create") {
            contentType(ContentType.Application.Json)
            body = options
        }["Id"] as String
    }

}