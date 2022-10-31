package org.katan.yoki.resource.system

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.katan.yoki.net.requestCatching

/**
 * Resource responsible for getting information from the operating system and the Docker daemon it is running on.
 */
public class SystemResource internal constructor(
    private val httpClient: HttpClient,
) {

    /**
     * Gets the version of Docker that is running and information about the system that Docker is running on.
     *
     * Corresponding CLI command:
     * ```sh
     * $ docker version
     * ```
     *
     * @throws org.katan.yoki.YokiResourceException If an error occurs in the request.
     */
    public suspend fun version(): SystemVersion {
        return requestCatching {
            httpClient.get("/version")
        }.body()
    }
}
