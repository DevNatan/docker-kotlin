@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.resource.secret

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.resource.ResourceIT
import org.katan.yoki.resource.swarm.NodeNotPartOfSwarmException
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
