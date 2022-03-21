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
import org.katan.yoki.resource.container.remove
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.fail

class RemoveContainerIT : BaseContainerIT() {

    @Test
    fun `throws ContainerNotFoundException on remove a unknown container`() = runTest {
        val client = createTestYoki()

        assertFailsWith(ContainerNotFoundException::class) {
            client.containers.remove(TEST_CONTAINER_NAME)
        }
    }

}
