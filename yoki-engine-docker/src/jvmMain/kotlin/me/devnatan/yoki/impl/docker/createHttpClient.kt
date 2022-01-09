package me.devnatan.yoki.impl.docker

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.serialization.kotlinx.json.*
import me.devnatan.yoki.api.*
import me.devnatan.yoki.protocol.*
import okhttp3.*
import java.io.*
import java.net.*
import javax.net.*

private class SocketDns : Dns {

    override fun lookup(hostname: String): List<InetAddress> {
        return if (hostname.endsWith(".socket")) listOf(
            InetAddress.getByAddress(
                hostname,
                byteArrayOf(0, 0, 0, 0)
            )
        ) else Dns.SYSTEM.lookup(hostname)
    }

}

public actual fun createHttpClient(yoki: Yoki<*, *>): HttpClient {
    return HttpClient(OkHttp) {
        engine {
            config {
                socketFactory(UnixSocketFactory())
                dns(SocketDns())
            }
        }
        defaultRequest {
            url {
                host = "localhost"
                port = 2375
            }
        }

        install(ContentNegotiation) { json() }
        install(UserAgent) { agent = "Yoki/0.0.1" }
    }
}