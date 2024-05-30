package me.devnatan.dockerkt

import me.devnatan.dockerkt.io.DEFAULT_DOCKER_HTTP_SOCKET
import me.devnatan.dockerkt.io.DEFAULT_DOCKER_UNIX_SOCKET
import me.devnatan.dockerkt.io.HTTP_SOCKET_PREFIX
import me.devnatan.dockerkt.io.UNIX_SOCKET_PREFIX
import kotlin.jvm.JvmStatic

internal val DefaultDocketClientConfig = DocketClientConfig.builder().forCurrentPlatform().build()

/**
 * Class to store all Docker client configurations.
 *
 * @param socketPath Docker socket file used to communicate with the main Docker daemon.
 *                   If not set, it will try to get from [DOCKER_HOST_ENV_KEY] environment variable, if it's found,
 *                   will try to select the socket path based on current operating system.
 * @param apiVersion The version of the Docker API that will be used during communication.
 *                   See more: [Versioned API and SDK](https://docs.docker.com/engine/api/#versioned-api-and-sdk).
 */
public class DocketClientConfig(
    public val socketPath: String,
    public val apiVersion: String,
    public val debugHttpCalls: Boolean,
) {
    init {
        check(socketPath.isNotBlank()) { "Socket path must be provided and cannot be blank" }
        check(apiVersion.isNotBlank()) { "Docker Remote API version must be provided and cannot be blank" }
    }

    public companion object {
        @JvmStatic
        public fun builder(): DockerClientConfigBuilder = DockerClientConfigBuilder()
    }
}

/**
 * Mutable builder for Docker client configuration.
 */
public class DockerClientConfigBuilder {
    /**
     * Docker socket file used to communicate with main Docker daemon.
     */
    private var socketPath: String = ""

    /**
     * The version of the Docker API that will be used during communication.
     */
    private var apiVersion: String =
        envOrFallback(
            key = DOCKER_API_VERSION_ENV_KEY,
            fallback = DEFAULT_DOCKER_API_VERSION,
            prefix = null,
        )

    /**
     * Whether to debug the HTTP calls to the Docker daemon.
     */
    private var debugHttpCalls: Boolean = false

    /**
     * Sets the Docker socket path.
     *
     * @param socketPath The Docker socket path.
     */
    public fun socketPath(socketPath: String): DockerClientConfigBuilder {
        this.socketPath = socketPath
        return this
    }

    /**
     * Sets the target Docker Remote API version.
     *
     * @param apiVersion Target Docker Remote API version.
     */
    public fun apiVersion(apiVersion: String): DockerClientConfigBuilder {
        this.apiVersion = apiVersion
        return this
    }

    /**
     * Sets the debug logging of HTTP calls.
     *
     * @param debugHttpCalls whether to log the HTTP calls
     */
    public fun debugHttpCalls(debugHttpCalls: Boolean = true): DockerClientConfigBuilder {
        this.debugHttpCalls = debugHttpCalls
        return this
    }

    /**
     * Configures to use a Unix socket defaults common to the standard Docker configuration.
     *
     * The socket path is defined to [DEFAULT_DOCKER_UNIX_SOCKET] if `DOCKER_HOST` env var is not set, or it doesn't
     * have the [UNIX_SOCKET_PREFIX] on its prefix.
     */
    public fun useUnixDefaults(): DockerClientConfigBuilder {
        socketPath =
            envOrFallback(
                key = DOCKER_HOST_ENV_KEY,
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
    public fun useHttpDefaults(): DockerClientConfigBuilder {
        socketPath =
            envOrFallback(
                key = DOCKER_HOST_ENV_KEY,
                fallback = DEFAULT_DOCKER_HTTP_SOCKET,
                prefix = HTTP_SOCKET_PREFIX,
            )
        return this
    }

    /**
     * Configures the [socketPath] based on the current platform.
     * See [selectDockerSocketPath] for implementation details.
     */
    public fun forCurrentPlatform(): DockerClientConfigBuilder {
        socketPath =
            envOrFallback(
                key = DOCKER_HOST_ENV_KEY,
                fallback = selectDockerSocketPath(),
                prefix = null,
            )
        return this
    }

    /**
     * Builds this class to a [DocketClientConfig].
     */
    public fun build(): DocketClientConfig {
        return DocketClientConfig(socketPath, apiVersion, debugHttpCalls)
    }

    /**
     * Returns the value for the given environment variable [key] or [fallback] if it isn't set.
     *
     * @param key The environment variable key.
     * @param fallback Fallback value if environment key is not set, or it's value don't start with [prefix].
     * @param prefix Prefix to check if the environment variable starts with.
     */
    private fun envOrFallback(
        key: String,
        fallback: String,
        prefix: String?,
    ): String {
        return env(key)?.ifBlank { null }?.let { path ->
            if (prefix == null || path.startsWith(prefix)) {
                path
            } else {
                null
            }
        } ?: fallback
    }

    /**
     * Selects a Docker socket path based on current OS.
     */
    private fun selectDockerSocketPath(): String {
        return if (isUnixPlatform()) {
            DEFAULT_DOCKER_UNIX_SOCKET
        } else {
            DEFAULT_DOCKER_HTTP_SOCKET
        }
    }

    public companion object {
        /**
         * Daemon socket to connect to.
         */
        private const val DOCKER_HOST_ENV_KEY = "DOCKER_HOST"

        /**
         * Override the negotiated Docker Remote API version.
         */
        private const val DOCKER_API_VERSION_ENV_KEY = "DOCKER_API_VERSION"

        /**
         * Minimum Docker Remote API version supported by docker-kotlin.
         */
        public const val DEFAULT_DOCKER_API_VERSION: String = "1.41"
    }
}
