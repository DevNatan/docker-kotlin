@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.engine.docker.resource.container

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.ContainerNotFoundException
import org.katan.yoki.containers
import org.katan.yoki.engine.docker.createTestYoki
import kotlin.test.Test
import kotlin.test.assertFailsWith

class RemoveContainerIT {

    @Test
    fun `throws ContainerNotFoundException on remove a unknown container`() = runTest {
        val client = createTestYoki()

        assertFailsWith(ContainerNotFoundException::class) {
            client.containers.remove("santo-bastao")
        }
    }
}
