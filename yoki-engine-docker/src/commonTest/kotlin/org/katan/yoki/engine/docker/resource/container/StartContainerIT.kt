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
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.fail

class StartContainerIT : BaseContainerIT() {

    @Test
    fun `successfully starts a container`() = runTest {
        val client = createTestYoki()
        val containerId = createTestContainer(client)

        try {
            client.containers.start(containerId)
        } catch (e: Throwable) {
            fail("Failed to start container", e)
        }
    }

    @Test
    fun `throws ContainerNotFoundException on try to start unknown container`() = runTest {
        val client = createTestYoki()

        assertFailsWith(ContainerNotFoundException::class) {
            client.containers.start(TEST_CONTAINER_NAME)
        }
    }

    @Test
    fun `throws ContainerAlreadyStartedException on try to start a already started container`() = runTest {
        val client = createTestYoki()
        val containerId = createTestContainer(client) {
            keepStartedForever()
        }

        try {
            client.containers.start(containerId)
        } catch (e: Throwable) {
            fail("Failed to start container", e)
        }

        assertFailsWith(ContainerAlreadyStartedException::class) {
            client.containers.start(containerId)
        }
    }
}
