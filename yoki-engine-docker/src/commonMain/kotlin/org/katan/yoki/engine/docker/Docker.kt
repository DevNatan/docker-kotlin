package org.katan.yoki.engine.docker

import io.ktor.client.*
import me.devnatan.yoki.api.*
import org.katan.api.*
import org.katan.yoki.*
import org.katan.yoki.api.*

public class Docker : Yoki<Docker> {

    private val httpClient = createHttpClient(this)
    override val engine: Docker get() = this
    override val config: YokiConfig = DefaultDockerConfig

    override fun close() {
        httpClient.close()
    }

}

public expect fun createHttpClient(yoki: Yoki<*>): HttpClient