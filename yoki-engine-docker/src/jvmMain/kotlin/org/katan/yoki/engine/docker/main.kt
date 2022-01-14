package org.katan.yoki.engine.docker

import kotlinx.coroutines.*
import org.katan.yoki.*
import org.katan.yoki.engine.docker.resource.network.*

public fun main() {
    runBlocking {
        val yoki = Yoki(Docker)
        val network = yoki.network.create {
            name = "yoki"
        }

        println("Network ${network.id} created")
        println(network.toString())

        val inspection = yoki.network.inspect(network.id)
        println("Network inspection")
        println(inspection.toString())
    }
}