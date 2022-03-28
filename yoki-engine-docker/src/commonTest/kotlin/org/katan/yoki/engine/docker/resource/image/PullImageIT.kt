@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.engine.docker.resource.image

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.engine.docker.createTestYoki
import org.katan.yoki.engine.docker.withImage
import org.katan.yoki.images
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
