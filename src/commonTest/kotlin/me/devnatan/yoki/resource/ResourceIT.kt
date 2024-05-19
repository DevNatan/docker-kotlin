package me.devnatan.yoki.resource

import me.devnatan.yoki.Yoki
import me.devnatan.yoki.createTestYoki

open class ResourceIT(
    private val debugHttpCalls: Boolean = false,
) {
    val testClient: Yoki by lazy {
        createTestYoki {
            debugHttpCalls(this@ResourceIT.debugHttpCalls)
        }
    }
}
