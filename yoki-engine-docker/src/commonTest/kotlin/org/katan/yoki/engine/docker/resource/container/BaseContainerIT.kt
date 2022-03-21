@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.engine.docker.resource.container

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.containers
import org.katan.yoki.engine.docker.TEST_CONTAINER_NAME
import org.katan.yoki.engine.docker.createTestYoki
import org.katan.yoki.resource.container.remove
import kotlin.test.AfterTest

open class BaseContainerIT internal constructor() {

    @AfterTest
    fun cleanup() = runTest {
        try {
            createTestYoki().containers.remove(TEST_CONTAINER_NAME) {
                removeAnonymousVolumes = true
                force = true
            }
        } catch (ignored: Throwable) {}
    }

}