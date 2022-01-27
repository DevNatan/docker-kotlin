package org.katan.yoki.util

import io.ktor.client.*
import org.katan.yoki.*

public expect fun createHttpClient(engine: DockerEngine): HttpClient
