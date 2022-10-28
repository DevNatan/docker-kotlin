@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.resource.image

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.createTestYoki
import org.katan.yoki.withImage
import kotlin.test.Test
import kotlin.test.assertTrue

class PullImageIT {

    @Test
    fun `pull image`() = runTest {
        val client = createTestYoki()

        client.withImage("busybox:latest") { imageTag ->
            assertTrue(
                client.images.list().any { it.repositoryTags.any { repoTag -> repoTag == imageTag } },
                "Pulled image must be in the images list"
            )
        }
    }
}
