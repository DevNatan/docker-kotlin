package org.katan.yoki.engine.docker.container

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import me.devnatan.yoki.api.external.properties.*
import me.devnatan.yoki.api.external.resource.container.*
import org.katan.api.external.properties.*
import org.katan.api.external.resource.container.*
import org.katan.yoki.api.external.properties.*
import org.katan.yoki.api.external.resource.container.*
import org.katan.yoki.external.properties.*
import org.katan.yoki.external.resource.container.*

/**
 * @see ContainerResource
 */
public class DockerContainerResource(private val httpClient: HttpClient) : ContainerResource {

    /**
     * Creates a new container.
     *
     * @see <a href="https://docs.podman.io/en/latest/_static/api.html#operation/ContainerCreateLibpod">ContainerCreateLibpod</a>
     */
    override suspend fun create(options: Map<String, Any>): String {
        require(ContainerImage in options) { "Container Image is required" }

        val response = httpClient.post("/containers/create") {
            contentType(ContentType.Application.Json)
            setBody(options)
        }

        println("Request to ${response.request.url}")

        return response.body<Map<String, Any>>()["Id"] as String
    }

}