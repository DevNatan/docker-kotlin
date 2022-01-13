package org.katan.yoki.engine.docker

import io.ktor.client.*
import org.katan.yoki.engine.*
import org.katan.yoki.engine.docker.resource.container.*
import org.katan.yoki.engine.docker.resource.network.*
import org.katan.yoki.engine.docker.resource.volume.*
import kotlin.coroutines.*

public class DockerEngine(
    public override val config: DockerEngineConfig
) : YokiEngine {

    public val httpClient: HttpClient by lazy { createHttpClient(this) }
    public override val coroutineContext: CoroutineContext by lazy { httpClient.coroutineContext }

    internal val containerResource = ContainerResource(this)
    internal val networkResource = NetworkResource(this)
    internal val volumeResource = VolumeResource(this)

}

public expect fun createHttpClient(engine: YokiEngine): HttpClient