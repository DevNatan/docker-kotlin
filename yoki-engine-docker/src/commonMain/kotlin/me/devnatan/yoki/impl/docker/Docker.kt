package me.devnatan.yoki.impl.docker

import io.ktor.client.*
import me.devnatan.yoki.api.*

public class Docker : Yoki<Docker> {

    private val httpClient = createHttpClient(this)
    override val engine: Docker get() = this
    override val config: YokiConfig = DefaultDockerConfig

    override fun close() {
        httpClient.close()
    }

}

public expect fun createHttpClient(yoki: Yoki<*>): HttpClient