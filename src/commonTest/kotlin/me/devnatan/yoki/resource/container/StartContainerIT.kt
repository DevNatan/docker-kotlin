@file:OptIn(ExperimentalCoroutinesApi::class)

package me.devnatan.yoki.resource.container

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.devnatan.yoki.keepStartedForever
import me.devnatan.yoki.resource.ResourceIT
import me.devnatan.yoki.withContainer
import kotlin.test.Test
import kotlin.test.assertFailsWith

class StartContainerIT : ResourceIT() {

    @Test
    fun `start a container`() = runTest {
        testClient.withContainer("busybox:latest") { id ->
            testClient.containers.start(id)
        }
    }

    @Test
    fun `throws ContainerNotFoundException on start unknown container`() = runTest {
        assertFailsWith<ContainerNotFoundException> {
            testClient.containers.start("jao-gomides" /* TODO generate random id */)
        }
    }

    @Test
    fun `throws ContainerAlreadyStartedException on start a already started container`() = runTest {
        testClient.withContainer(
            image = "busybox:latest",
            options = { keepStartedForever() },
        ) { container ->
            testClient.containers.start(container)

            assertFailsWith<ContainerAlreadyStartedException> {
                testClient.containers.start(container)
            }
        }
    }
}
