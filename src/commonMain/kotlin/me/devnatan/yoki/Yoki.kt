package me.devnatan.yoki

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import me.devnatan.yoki.YokiConfigBuilder.Companion.DEFAULT_DOCKER_API_VERSION
import me.devnatan.yoki.logging.Logger
import me.devnatan.yoki.logging.createLogger
import me.devnatan.yoki.net.createHttpClient
import me.devnatan.yoki.resource.container.ContainerResource
import me.devnatan.yoki.resource.exec.ExecResource
import me.devnatan.yoki.resource.image.ImageResource
import me.devnatan.yoki.resource.network.NetworkResource
import me.devnatan.yoki.resource.secret.SecretResource
import me.devnatan.yoki.resource.system.SystemResource
import me.devnatan.yoki.resource.volume.VolumeResource
import me.devnatan.yoki.util.Closeable
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * Creates a new Yoki instance with platform default socket path and [DEFAULT_DOCKER_API_VERSION] Docker API version
 * that'll be merged with specified configuration.
 *
 * @param config Yoki configuration.
 */
public inline fun Yoki(
    crossinline config: YokiConfigBuilder.() -> Unit = {},
): Yoki {
    return Yoki(
        YokiConfigBuilder()
            .forCurrentPlatform()
            .apply(config)
            .build(),
    )
}

/**
 * Yoki heart, where all resource accessors and other things are located.
 *
 * Create and configure a fresh Yoki instance by calling [Yoki.create] or [org.katan.yoki.Yoki]
 *
 * Note: This class must be a singleton, that is, don't instantiate it more than once in your code, and, implements
 * [Closeable] so be sure to [close] it after use.
 */
@YokiDsl
public class Yoki @PublishedApi internal constructor(
    public val config: YokiConfig,
) : Closeable {

    private val httpClient: HttpClient = createHttpClient(this)
    private val json: Json = Json {
        ignoreUnknownKeys = true
    }
    private val logger: Logger = createLogger()

    @JvmField
    public val containers: ContainerResource = ContainerResource(httpClient, json, logger)

    @JvmField
    public val networks: NetworkResource = NetworkResource(httpClient, json)

    @JvmField
    public val volumes: VolumeResource = VolumeResource(httpClient, json)

    @JvmField
    public val secrets: SecretResource = SecretResource(httpClient, json)

    @JvmField
    public val images: ImageResource = ImageResource(httpClient, json)

    @JvmField
    public val exec: ExecResource = ExecResource(httpClient)

    @JvmField
    public val system: SystemResource = SystemResource(httpClient)

    public override fun close() {
        httpClient.close()
    }

    public companion object {

        /**
         * Creates a new Yoki instance with platform default socket path and targeting [DEFAULT_DOCKER_API_VERSION]
         * Docker API version.
         */
        @JvmStatic
        public fun create(): Yoki {
            return Yoki()
        }

        /**
         * Creates a new Yoki instance.
         *
         * @param config Configurations to the instance.
         */
        @JvmStatic
        public fun create(config: YokiConfig): Yoki {
            return Yoki(config)
        }

        /**
         * Creates a new Yoki instance with the specified socket path configuration.
         *
         * @param socketPath The socket path that'll be used on connection.
         */
        @JvmStatic
        public fun create(socketPath: String): Yoki {
            return Yoki {
                this.socketPath = socketPath
            }
        }

        /**
         * Creates a new Yoki instance using UNIX defaults configuration.
         */
        @JvmStatic
        public fun createWithUnixDefaults(): Yoki {
            return Yoki { useUnixDefaults() }
        }

        /**
         * Creates a new Yoki instance using HTTP defaults configuration.
         */
        @JvmStatic
        public fun createWithHttpDefaults(): Yoki {
            return Yoki { useHttpDefaults() }
        }
    }
}

/**
 * DslMarker for Yoki.
 */
@DslMarker
public annotation class YokiDsl
