package org.katan.yoki

import org.katan.yoki.io.DEFAULT_DOCKER_UNIX_SOCKET

private const val DOCKER_HOST_ENV_KEY = "DOCKER_HOST"
private const val UNIX_SOCKET_PREFIX = "unix://"

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
        socketPath = env(DOCKER_HOST_ENV_KEY)?.ifBlank { null }?.let { path ->
            if (path.startsWith(UNIX_SOCKET_PREFIX)) path
            else null
        } ?: DEFAULT_DOCKER_UNIX_SOCKET
    }
}
