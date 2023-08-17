package me.devnatan.yoki

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.serialization.json.Json
import me.devnatan.yoki.io.createHttpClient
import me.devnatan.yoki.resource.container.ContainerResource
import me.devnatan.yoki.resource.exec.ExecResource
import me.devnatan.yoki.resource.image.ImageResource
import me.devnatan.yoki.resource.network.NetworkResource
import me.devnatan.yoki.resource.secret.SecretResource
import me.devnatan.yoki.resource.system.SystemResource
import me.devnatan.yoki.resource.volume.VolumeResource
import kotlin.coroutines.CoroutineContext

public actual class Yoki public actual constructor(public actual val config: YokiConfig) : CoroutineScope {

    public constructor() : this(DefaultYokiConfig)

    override val coroutineContext: CoroutineContext = SupervisorJob()

    public actual val json: Json = Json { ignoreUnknownKeys = true }
    public actual val httpClient: HttpClient = createHttpClient(this)

    @get:JvmName("images")
    public actual val images: ImageResource = ImageResource(httpClient, json)

    @get:JvmName("exec")
    public actual val exec: ExecResource = ExecResource(httpClient)

    @get:JvmName("containers")
    public actual val containers: ContainerResource = ContainerResource(this, json, httpClient)

    @get:JvmName("networks")
    public actual val networks: NetworkResource = NetworkResource(httpClient, json)

    @get:JvmName("volumes")
    public actual val volumes: VolumeResource = VolumeResource(httpClient, json)

    @get:JvmName("secrets")
    public actual val secrets: SecretResource = SecretResource(httpClient, json)

    @get:JvmName("system")
    public actual val system: SystemResource = SystemResource(httpClient, json)

    public actual fun close() {
        cancel()
        httpClient.close()
    }
}
