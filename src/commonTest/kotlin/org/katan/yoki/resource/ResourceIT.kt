package org.katan.yoki.resource

import org.katan.yoki.Yoki
import org.katan.yoki.createTestYoki

open class ResourceIT {

    companion object {
        val testClient: Yoki by lazy(::createTestYoki)
    }
}
