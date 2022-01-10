package org.katan.yoki.engine.docker

import io.ktor.client.*
import org.katan.yoki.api.*

public class DockerEngine : YokiEngine {

    internal val httpClient: HttpClient = createHttpClient(this)

    public override fun close() {
        httpClient.close()
    }

}

public expect fun createHttpClient(engine: YokiEngine): HttpClient

public fun test() {

}