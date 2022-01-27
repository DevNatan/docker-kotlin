package org.katan.yoki

import io.ktor.client.*
import io.ktor.network.selector.*
import org.katan.yoki.*

public expect fun createHttpClient(engine: DockerEngine): HttpClient
