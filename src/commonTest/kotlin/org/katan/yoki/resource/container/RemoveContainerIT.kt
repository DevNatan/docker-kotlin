@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.resource.container

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.resource.ContainerNotFoundException
import org.katan.yoki.resource.ResourceIT
import kotlin.test.Test
import kotlin.test.assertFailsWith

class RemoveContainerIT : ResourceIT() {

    @Test
    fun `throws ContainerNotFoundException on remove a unknown container`() = runTest {
        assertFailsWith<ContainerNotFoundException>() {
            testClient.containers.remove("santo-bastao"/* TODO generate random id */)
        }
    }
}
