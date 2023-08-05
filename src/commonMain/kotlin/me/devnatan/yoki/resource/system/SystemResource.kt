package me.devnatan.yoki.resource.system

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.head
import me.devnatan.yoki.models.system.SystemPingData
import me.devnatan.yoki.models.system.SystemVersion
import me.devnatan.yoki.net.requestCatching
import kotlin.jvm.JvmOverloads

/**
 * Resource responsible for getting information from the operating system and the Docker daemon it is running on.
 */
public class SystemResource internal constructor(
    private val httpClient: HttpClient,
) {

    private companion object {
        const val PING_ENDPOINT = "/_ping"
    }

    /**
     * Gets the version of Docker that is running and information about the system that Docker is running on.
     *
     * Corresponding CLI command:
     * ```sh
     * $ docker version
     * ```
     *
     * @throws me.devnatan.yoki.YokiResourceException If an error occurs in the request.
     */
    public suspend fun version(): SystemVersion {
        return requestCatching {
            httpClient.get("/version")
        }.body()
    }

    /**
     * Pings the server completing successfully if the server is accessible.
     *
     * @param head Should use `HEAD` instead of `GET` HTTP method to ping.
     */
    @JvmOverloads
    public suspend fun ping(head: Boolean = true): SystemPingData {
        return requestCatching {
            if (head) {
                httpClient.head(PING_ENDPOINT)
            } else {
                httpClient.get(PING_ENDPOINT)
            }
        }.headers.let { headers ->
            SystemPingData(
                apiVersion = headers["API-Version"].orEmpty(),
                builderVersion = headers["Builder-Version"].orEmpty(),
                experimental = headers["Docker-Experimental"]?.toBooleanStrict() ?: false,
            )
        }
    }
}
