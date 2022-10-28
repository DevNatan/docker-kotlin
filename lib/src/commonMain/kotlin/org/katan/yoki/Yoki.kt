package org.katan.yoki

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.katan.yoki.io.createHttpClient
import org.katan.yoki.io.resourceExceptionJsonDeserializer
import org.katan.yoki.resource.container.ContainerResource
import org.katan.yoki.resource.image.ImageResource
import org.katan.yoki.resource.network.NetworkResource
import org.katan.yoki.resource.secret.SecretResource
import org.katan.yoki.resource.volume.VolumeResource
import kotlin.coroutines.CoroutineContext

public class Yoki @PublishedApi internal constructor(
    public val config: YokiConfig
) : CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = CoroutineName("Yoki") + job

    private val httpClient: HttpClient = createHttpClient(this)
    internal val json: Json = resourceExceptionJsonDeserializer

    public val containers: ContainerResource = ContainerResource(httpClient, json)
    public val networks: NetworkResource = NetworkResource(httpClient, json)
    public val volumes: VolumeResource = VolumeResource(httpClient, json)
    public val secrets: SecretResource = SecretResource(httpClient, json)
    public val images: ImageResource = ImageResource(httpClient, json)
}

@YokiDsl
public inline fun Yoki(block: YokiConfig.() -> Unit = {}): Yoki {
    val config = YokiConfig().apply(block)
    return Yoki(config)
}

/**
 * DslMarker for Yoki.
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
public annotation class YokiDsl
