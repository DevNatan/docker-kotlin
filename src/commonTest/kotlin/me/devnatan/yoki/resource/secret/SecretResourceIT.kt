@file:OptIn(ExperimentalCoroutinesApi::class)

package me.devnatan.yoki.resource.secret

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.devnatan.yoki.resource.NodeNotPartOfSwarmException
import me.devnatan.yoki.resource.ResourceIT
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SecretResourceIT : ResourceIT() {

    @Test
    fun `list secrets with no swarm`() = runTest {
        assertFailsWith<NodeNotPartOfSwarmException> {
            testClient.secrets.list()
        }
    }
}
