package org.katan.yoki

import org.katan.yoki.io.DEFAULT_DOCKER_HTTP_SOCKET
import org.katan.yoki.io.DEFAULT_DOCKER_UNIX_SOCKET
import org.katan.yoki.io.HTTP_SOCKET_PREFIX
import org.katan.yoki.io.UNIX_SOCKET_PREFIX

private const val DOCKER_HOST_ENV_KEY = "DOCKER_HOST"

public class YokiConfig @PublishedApi internal constructor() {

    public var socketPath: String = ""

    /**
     * The version of the Docker API that will be used during communication.
     *
     * @see <a href="https://docs.docker.com/engine/api/#versioned-api-and-sdk">Versioned API and SDK</a>
     */
    public var apiVersion: String = "1.41"

    /**
     * Configures to use a Unix socket defaults common to the standard Docker configuration.
     *
     * The socket path is defined to [DEFAULT_DOCKER_UNIX_SOCKET] if `DOCKER_HOST` env var is not set,
     * or it doesn't have the [UNIX_SOCKET_PREFIX].
     */
    public fun withUnixDefaults() {
        socketPath = dockerHostOrFallback(
            fallback = DEFAULT_DOCKER_UNIX_SOCKET,
            prefix = UNIX_SOCKET_PREFIX,
        )
    }

    public fun withHttpDefaults() {
        socketPath = dockerHostOrFallback(
            fallback = DEFAULT_DOCKER_HTTP_SOCKET,
            prefix = HTTP_SOCKET_PREFIX,
        )
    }

    private fun dockerHostOrFallback(
        fallback: String,
        prefix: String,
    ): String {
        return env(DOCKER_HOST_ENV_KEY)?.ifBlank { null }?.let { path ->
            if (path.startsWith(prefix)) path
            else null
        } ?: fallback
    }
}
