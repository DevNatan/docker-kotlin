package me.devnatan.yoki.resource

import me.devnatan.yoki.Yoki
import me.devnatan.yoki.createTestYoki

open class ResourceIT {

    companion object {
        val testClient: Yoki by lazy(::createTestYoki)
    }
}
