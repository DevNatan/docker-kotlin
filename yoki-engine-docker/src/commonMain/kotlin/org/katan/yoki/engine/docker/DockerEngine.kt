package org.katan.yoki.engine.docker

import io.ktor.client.*
import org.katan.yoki.api.*

public class DockerEngine(
    internal val httpClient: HttpClient
) : YokiEngine {

    public override fun close() {
        httpClient.close()
    }

}

public expect fun createHttpClient(client: Yoki): HttpClient