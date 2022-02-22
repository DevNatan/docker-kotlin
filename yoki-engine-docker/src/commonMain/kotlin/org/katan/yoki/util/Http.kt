package org.katan.yoki.util

import io.ktor.client.HttpClient
import org.katan.yoki.DockerEngine

public expect fun createHttpClient(engine: DockerEngine): HttpClient
