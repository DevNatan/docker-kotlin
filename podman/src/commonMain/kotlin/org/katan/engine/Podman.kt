package org.katan.engine

import io.ktor.client.*
import io.ktor.client.features.json.*
import org.katan.engine.Engine
import org.katan.engine.container.ContainerFactory
import org.katan.engine.container.PodmanContainerFactory

/**
 * @see Engine
 */
class Podman : Engine {

    private val httpClient = HttpClient {
        bas
        install(JsonFeature)
    }

    override val containerFactory: ContainerFactory = PodmanContainerFactory(httpClient)

}