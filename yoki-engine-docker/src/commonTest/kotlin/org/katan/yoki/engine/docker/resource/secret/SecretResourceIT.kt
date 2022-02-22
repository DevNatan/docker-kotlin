@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.engine.docker.resource.secret

import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.katan.yoki.*
import org.katan.yoki.engine.docker.*
import kotlin.test.*

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
