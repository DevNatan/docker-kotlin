@file:JvmMultifileClass
@file:JvmName("SocketPathUtils")

package org.katan.yoki

import org.katan.yoki.net.DEFAULT_DOCKER_HTTP_SOCKET
import org.katan.yoki.net.DEFAULT_DOCKER_UNIX_SOCKET
import org.katan.yoki.net.HTTP_SOCKET_PREFIX
import org.katan.yoki.net.UNIX_SOCKET_PREFIX
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

private const val DOCKER_HOST_ENV_KEY = "DOCKER_HOST"

/**
 * Class to store all Yoki configurations.
 *
 * @param socketPath Docker socket file used to communicate with the main Docker daemon.
 *                   If not set, it will try to get from [DOCKER_HOST_ENV_KEY] environment variable, if it's found,
 *                   will try to select the socket path based on current operating system.
 * @param apiVersion The version of the Docker API that will be used during communication.
 *                   See more: [Versioned API and SDK](https://docs.docker.com/engine/api/#versioned-api-and-sdk).
 */
public class YokiConfig(
    public val socketPath: String,
    public val apiVersion: String,
) {

    init {
        check(socketPath.isNotBlank()) { "Socket path must be provided and cannot be blank" }
        check(apiVersion.isNotBlank()) { "Docker Remote API version must be provided and cannot be blank" }
    }
}

/**
 * Mutable builder for Yoki configuration.
 */
public class YokiConfigBuilder {

    public companion object {
        public const val DEFAULT_DOCKER_API_VERSION: String = "1.41"
    }

    /**
     * Docker socket file used to communicate with main Docker daemon.
     */
    public var socketPath: String = ""

    /**
     * The version of the Docker API that will be used during communication.
     */
    public var apiVersion: String = ""

    /**
     * Sets the Docker socket path.
     *
     * @param socketPath The Docker socket path.
     */
    public fun socketPath(socketPath: String): YokiConfigBuilder {
        this.socketPath = socketPath
        return this
    }

    /**
     * Sets the target Docker Remote API version.
     *
     * @param apiVersion Target Docker Remote API version.
     */
    public fun apiVersion(apiVersion: String): YokiConfigBuilder {
        this.apiVersion = apiVersion
        return this
    }

    /**
     * Configures to use a Unix socket defaults common to the standard Docker configuration.
     *
     * The socket path is defined to [DEFAULT_DOCKER_UNIX_SOCKET] if `DOCKER_HOST` env var is not set, or it doesn't
     * have the [UNIX_SOCKET_PREFIX] on its prefix.
     */
    public fun useUnixDefaults(): YokiConfigBuilder {
        socketPath = dockerHostOrFallback(
            fallback = DEFAULT_DOCKER_UNIX_SOCKET,
            prefix = UNIX_SOCKET_PREFIX,
        )
        return this
    }

    /**
     * Configures to use an HTTP socket defaults common to the standard Docker configuration.
     *
     * The socket path is defined to [DEFAULT_DOCKER_HTTP_SOCKET] if `DOCKER_HOST` env var is not set, or it doesn't
     * have the [HTTP_SOCKET_PREFIX] on its prefix.
     */
    public fun useHttpDefaults(): YokiConfigBuilder {
        socketPath = dockerHostOrFallback(
            fallback = DEFAULT_DOCKER_HTTP_SOCKET,
            prefix = HTTP_SOCKET_PREFIX,
        )
        return this
    }

    /**
     * Configures the [socketPath] based on the current platform. See [selectDockerSocketPath] for implementation details.
     */
    public fun forCurrentPlatform(): YokiConfigBuilder {
        socketPath = dockerHostOrFallback(selectDockerSocketPath(), null)
        return this
    }

    /**
     * Builds this class to a [YokiConfig].
     */
    public fun build(): YokiConfig {
        return YokiConfig(socketPath, apiVersion)
    }
}

/**
 * Returns the Docker socket path defined on [DOCKER_HOST_ENV_KEY] environment variable, [fallback] if it isn't set.
 *
 * @param fallback Fallback value if environment key is not set, or it's value don't start with [prefix].
 * @param prefix Prefix to check if the environment variable starts with.
 */
private fun dockerHostOrFallback(
    fallback: String,
    prefix: String?,
): String {
    return env(DOCKER_HOST_ENV_KEY)?.ifBlank { null }?.let { path ->
        if (prefix == null || path.startsWith(prefix)) path
        else null
    } ?: fallback
}

/**
 * Selects a Docker socket path based on current OS.
 */
private fun selectDockerSocketPath(): String {
    return if (isUnixPlatform())
        DEFAULT_DOCKER_UNIX_SOCKET
    else
        DEFAULT_DOCKER_HTTP_SOCKET
}
