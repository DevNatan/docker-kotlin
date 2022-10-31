@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.resource.image

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.resource.ResourceIT
import org.katan.yoki.withImage
import kotlin.test.Test
import kotlin.test.assertTrue

class PullImageIT : ResourceIT() {

    @Test
    fun `image pull`() = runTest {
        testClient.withImage("busybox:latest") { imageTag ->
            assertTrue(
                testClient.images.list().any { it.repositoryTags.any { repoTag -> repoTag == imageTag } },
                "Pulled image must be in the images list"
            )
        }
    }
}
