package me.devnatan.yoki

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import me.devnatan.yoki.resource.container.ContainerResource
import me.devnatan.yoki.resource.exec.ExecResource
import me.devnatan.yoki.resource.image.ImageResource
import me.devnatan.yoki.resource.network.NetworkResource
import me.devnatan.yoki.resource.secret.SecretResource
import me.devnatan.yoki.resource.system.SystemResource
import me.devnatan.yoki.resource.volume.VolumeResource

public expect class Yoki(config: YokiConfig) : CoroutineScope {

    public val config: YokiConfig
    public val json: Json
    public val httpClient: HttpClient
    public val images: ImageResource
    public val exec: ExecResource
    public val containers: ContainerResource
    public val networks: NetworkResource
    public val volumes: VolumeResource
    public val secrets: SecretResource
    public val system: SystemResource

    public fun close()
}
