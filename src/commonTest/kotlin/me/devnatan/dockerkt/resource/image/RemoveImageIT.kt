@file:OptIn(ExperimentalCoroutinesApi::class)

package me.devnatan.dockerkt.resource.image

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import me.devnatan.dockerkt.resource.ResourceIT
import kotlin.test.Test
import kotlin.test.fail

class RemoveImageIT : ResourceIT() {
    @Test
    fun `image remove`() =
        runTest {
            val image = "busybox:latest"

            try {
                testClient.images.pull(image).collect()
            } catch (e: Throwable) {
                fail("Failed to pull image", e)
            }

            testClient.images.remove(image)
        }
}
