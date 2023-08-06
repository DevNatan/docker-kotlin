package me.devnatan.yoki

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import me.devnatan.yoki.YokiConfigBuilder.Companion.DEFAULT_DOCKER_API_VERSION
import me.devnatan.yoki.io.createHttpClient
import me.devnatan.yoki.logging.Logger
import me.devnatan.yoki.logging.createLogger
import me.devnatan.yoki.resource.container.ContainerResource
import me.devnatan.yoki.resource.exec.ExecResource
import me.devnatan.yoki.resource.image.ImageResource
import me.devnatan.yoki.resource.network.NetworkResource
import me.devnatan.yoki.resource.secret.SecretResource
import me.devnatan.yoki.resource.system.SystemResource
import me.devnatan.yoki.resource.volume.VolumeResource
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
 * Yoki's heart where all resource accessors and other things are located.
 *
 * Create and configure a fresh Yoki instance by calling [Yoki.create] or [me.devnatan.yoki.Yoki].
 *
 * Note: This class must be a singleton, that is, don't instantiate it more than once in your code, and, implements
 * [Closeable] so be sure to [close] it after use.
 */
@YokiDsl
public class Yoki @PublishedApi internal constructor(public val config: YokiConfig) :
    Closeable, CoroutineScope by CoroutineScope(SupervisorJob()) {

    private val httpClient: HttpClient = createHttpClient(this)
    private val json: Json = Json {
        ignoreUnknownKeys = true
    }
    private val logger: Logger = createLogger()

    @get:JvmName("containers")
    public val containers: ContainerResource = ContainerResource(httpClient, json, logger, this)

    @get:JvmName("networks")
    public val networks: NetworkResource = NetworkResource(httpClient, json)

    @get:JvmName("volumes")
    public val volumes: VolumeResource = VolumeResource(httpClient, json)

    @get:JvmName("pirocoptero")
    @get:JvmSynthetic
    public val javaVolumes: VolumeResource = VolumeResource(httpClient, json)

    @get:JvmName("secrets")
    public val secrets: SecretResource = SecretResource(httpClient, json)

    @get:JvmName("images")
    public val images: ImageResource = ImageResource(httpClient, json)

    @get:JvmName("exec")
    public val exec: ExecResource = ExecResource(httpClient)

    @get:JvmName("system")
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
        public fun create(): Yoki = Yoki()

        /**
         * Creates a new Yoki instance.
         *
         * @param config Configurations to the instance.
         */
        @JvmStatic
        public fun create(config: YokiConfig): Yoki = Yoki(config)

        /**
         * Creates a new Yoki instance with the specified socket path configuration.
         *
         * @param socketPath The socket path that'll be used on connection.
         */
        @JvmStatic
        public fun create(socketPath: String): Yoki = Yoki { socketPath(socketPath) }

        /**
         * Creates a new Yoki instance using UNIX defaults configuration.
         */
        @JvmStatic
        public fun createWithUnixDefaults(): Yoki = Yoki { useUnixDefaults() }

        /**
         * Creates a new Yoki instance using HTTP defaults configuration.
         */
        @JvmStatic
        public fun createWithHttpDefaults(): Yoki = Yoki { useHttpDefaults() }
    }
}

/**
 * DslMarker for Yoki.
 */
@DslMarker
public annotation class YokiDsl
