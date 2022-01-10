package org.katan.yoki.engine.docker.container

import io.kotest.common.*
import io.kotest.core.spec.style.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*

@ExperimentalKotest
class ContainerResourceTests : FunSpec() {
    init {
        testCoroutineDispatcher = true

        test("foo").config(testCoroutineDispatcher = true) {
            val mockEngine = MockEngine { request ->
                respond(
                    content = ByteReadChannel("""
                        {"Id":"a94832"}
                    """.trimIndent()),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }
        }
    }
}