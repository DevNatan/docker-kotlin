package org.katan.yoki

import io.ktor.client.*
import kotlinx.serialization.json.*
import org.katan.yoki.resource.container.*
import org.katan.yoki.resource.network.*
import org.katan.yoki.resource.secret.*
import org.katan.yoki.resource.volume.*
import kotlin.coroutines.*

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
