package org.katan.yoki

import org.katan.yoki.engine.YokiEngine
import org.katan.yoki.engine.YokiEngineConfig
import org.katan.yoki.resource.container.ContainerResource
import org.katan.yoki.resource.network.NetworkResource
import org.katan.yoki.resource.secret.SecretResource
import org.katan.yoki.resource.volume.VolumeResource
import org.katan.yoki.util.createHttpClient
import kotlin.coroutines.*
import io.ktor.client.*
import kotlinx.serialization.json.*

public class DockerEngine(
    public override val config: DockerEngineConfig
) : YokiEngine {

    public val json: Json = Json { ignoreUnknownKeys = true }
    public val httpClient: HttpClient by lazy { createHttpClient(this) }

    public override val coroutineContext: CoroutineContext by lazy { httpClient.coroutineContext }

    internal val containerResource by lazy { ContainerResource(this) }
    internal val networkResource by lazy { NetworkResource(this) }
    internal val volumeResource by lazy { VolumeResource(this) }
    internal val secretResource by lazy { SecretResource(this) }
}

/**
 * Docker engine configuration
 *
 * @see DockerEngine
 */
public class DockerEngineConfig : YokiEngineConfig() {

    /**
     * The version of the Docker API that will be used during communication.
     *
     * @see <a href="https://docs.docker.com/engine/api/#versioned-api-and-sdk">Versioned API and SDK</a>
     */
    public var apiVersion: String = "1.41"

    public var socketPath: String = "unix:///var/run/docker.sock"
}
