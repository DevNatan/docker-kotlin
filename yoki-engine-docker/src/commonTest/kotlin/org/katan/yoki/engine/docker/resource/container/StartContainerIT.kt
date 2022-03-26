@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.engine.docker.resource.container

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.ContainerAlreadyStartedException
import org.katan.yoki.ContainerNotFoundException
import org.katan.yoki.containers
import org.katan.yoki.engine.docker.TEST_CONTAINER_NAME
import org.katan.yoki.engine.docker.createTestContainer
import org.katan.yoki.engine.docker.createTestYoki
import org.katan.yoki.engine.docker.keepStartedForever
import org.katan.yoki.engine.docker.withContainer
import kotlin.test.Test
import kotlin.test.assertFailsWith

class StartContainerIT : BaseContainerIT() {

    @Test
    fun `successfully starts a container`() = runTest {
        val client = createTestYoki()

        client.withContainer("busybox:latest") { id ->
            client.containers.start(id)
        }
    }

    @Test
    fun `throws ContainerNotFoundException on start unknown container`() = runTest {
        val client = createTestYoki()

        assertFailsWith(ContainerNotFoundException::class) {
            client.containers.start(TEST_CONTAINER_NAME)
        }
    }

    @Test
    fun `throws ContainerAlreadyStartedException on start a already started container`() = runTest {
        val client = createTestYoki()
        val containerId = createTestContainer(client) {
            keepStartedForever()
        }

        client.containers.start(containerId)

        assertFailsWith(ContainerAlreadyStartedException::class) {
            client.containers.start(containerId)
        }
    }
}
