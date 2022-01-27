package org.katan.yoki

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.katan.yoki.*
import org.katan.yoki.engine.docker.model.container.*
import org.katan.yoki.engine.docker.resource.container.*

private val json: Json = Json {
    prettyPrint = true
    prettyPrintIndent = "  "
}

public fun main(args: Array<out String>): Unit = runBlocking {
    Yoki(Docker).use { yoki ->
        when (args.getOrNull(0) ?: "list") {
            "list" -> yoki.listContainers()
            "create" -> yoki.createContainer(args.getOrNull(1))
            "inspect" -> yoki.inspectContainer(args[1])
            "logs" -> yoki.logsContainer(args[1], args.getOrNull(2))
            else -> println("Nothing given")
        }
    }
}

private suspend fun Yoki.createContainer(image: String?) {
    val container = containers.create {
        hostname = "a"
    }
    val container = containers.create(ContainerCreateOptions(image = (image ?: "busybox")))

    println("Container created:")
    println(json.encodeToString(container))
}

private suspend fun Yoki.inspectContainer(id: String) {
    val container = containers.inspect(id)

    println("Container inspection result:")
    println(json.encodeToString(container))
}

private suspend fun Yoki.logsContainer(id: String, streaming: String?) {
    val stream = streaming?.toBooleanStrictOrNull() ?: false
    val logs = containers.logs(id) {
        follow = stream
        stdout = true
        showTimestamps = true
    }

    println("Container logs (streaming: $stream):")
    logs.onStart {
        println("--- Flow started ---")
    }.onCompletion {
        println("--- Flow completed ---")
    }.collect {
        println("[${it.stream.name}] ${it.value}")
    }
}

private suspend fun Yoki.listContainers() {
    val containers = containers.list {
        all = true
    }

    val containerList = containers.list(
        ContainerListOptions(
            all = true
        )
    )
    println("Container list:")
    println(json.encodeToString(containerList))
}
