package org.katan.yoki.engine.docker

import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.katan.yoki.*

public fun main() {
    val yoki = Yoki(Docker)
    val container = runBlocking {
        yoki.containers.create(mapOf(
            "image" to "itzg/minecraft-server",
            "name" to "yoki-test"
        ))
    }

    println("Created container:")
    println(Json { prettyPrint = true }.encodeToString(container) )
}