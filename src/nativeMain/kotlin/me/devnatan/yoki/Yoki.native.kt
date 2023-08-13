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

public actual class Yoki public actual constructor(public actual val config: YokiConfig) : CoroutineScope, Closeable {

    override val coroutineContext: CoroutineContext = SupervisorJob()

    public actual val json: Json = Json { ignoreUnknownKeys = true }
    public actual val httpClient: HttpClient = createHttpClient(this)

    public actual val images: ImageResource = ImageResource(httpClient, json)
    public actual val exec: ExecResource = ExecResource(httpClient)
    public actual val containers: ContainerResource = ContainerResource()
    public actual val networks: NetworkResource = NetworkResource(httpClient, json)
    public actual val volumes: VolumeResource = VolumeResource(httpClient, json)
    public actual val secrets: SecretResource = SecretResource(httpClient, json)
    public actual val system: SystemResource = SystemResource(httpClient, json)

    actual override fun close() {
        cancel()
    }
}
