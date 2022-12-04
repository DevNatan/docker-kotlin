@file:OptIn(ExperimentalCoroutinesApi::class)

package me.devnatan.yoki.resource.image

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.devnatan.yoki.resource.ResourceIT
import me.devnatan.yoki.withImage
import kotlin.test.Test
import kotlin.test.assertTrue

class PullImageIT : ResourceIT() {

    @Test
    fun `image pull`() = runTest {
        testClient.withImage("busybox:latest") { imageTag ->
            assertTrue(
                actual = testClient.images.list().any { it.repositoryTags.any { repoTag -> repoTag == imageTag } },
                message = "Pulled image must be in the images list",
            )
        }
    }
}
