package me.devnatan.yokitest

import kotlinx.coroutines.runBlocking
import org.katan.yoki.Yoki

public fun main(): Unit = runBlocking {
    val yoki = Yoki {
        withUnixDefaults()
    }

    println(yoki.containers.list())
}
