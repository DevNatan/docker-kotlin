package org.katan.yoki.engine.docker.resource.network

import org.katan.yoki.engine.docker.*
import org.katan.yoki.engine.docker.network.model.*

public class NetworkResource(private val engine: DockerEngine) {

    public suspend fun list(options: Map<String, Any>): List<Network> {
        return engine.httpClient.get("/networks")
    }

}