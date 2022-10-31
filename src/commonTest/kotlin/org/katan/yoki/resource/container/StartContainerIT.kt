@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.resource.container

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.keepStartedForever
import org.katan.yoki.resource.ResourceIT
import org.katan.yoki.withContainer
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
        testClient.withContainer("busybox:latest", {
            keepStartedForever()
        }) { container ->
            testClient.containers.start(container)

            assertFailsWith<ContainerAlreadyStartedException> {
                testClient.containers.start(container)
            }
        }
    }
}
