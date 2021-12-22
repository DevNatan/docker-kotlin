package org.katan.engine.container

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

/**
 * @see PodmanContainerFactory
 */
class PodmanContainerFactoryTests {

    @Test
    fun testContainerCreate() = runBlocking {
        val httpClient = createHttpClient { request ->
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
    }

    private fun createHttpClient(engineHandler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData): HttpClient {
        return HttpClient(MockEngine.Companion(engineHandler)) {
            install(JsonFeature)
        }
    }

}