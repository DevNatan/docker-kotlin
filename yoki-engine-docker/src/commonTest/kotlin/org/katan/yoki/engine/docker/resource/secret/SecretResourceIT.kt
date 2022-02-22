@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.engine.docker.resource.secret

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.engine.docker.createTestYoki
import org.katan.yoki.secrets
import kotlin.test.Test
import kotlin.test.fail

class SecretResourceIT {

    @Test
    fun `list secrets`() = runTest {
        val client = createTestYoki()

        // will throw exception on fail
        runCatching {
            client.secrets.list()
        }.onFailure {
            fail("Failed to list secrets")
        }
    }
}
