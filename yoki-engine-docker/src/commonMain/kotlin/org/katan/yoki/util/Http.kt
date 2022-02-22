package org.katan.yoki.util

import org.katan.yoki.*
import io.ktor.client.*

public expect fun createHttpClient(engine: DockerEngine): HttpClient
