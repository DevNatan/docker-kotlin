@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.resource.secret

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.createTestYoki
import kotlin.test.Test
import kotlin.test.assertFails

class SecretResourceIT {

    @Test
    fun `list secrets with no swarm`() = runTest {
        val client = createTestYoki()
        assertFails("Secrets endpoint only work with swarm enabled") { client.secrets.list() }
    }
}
