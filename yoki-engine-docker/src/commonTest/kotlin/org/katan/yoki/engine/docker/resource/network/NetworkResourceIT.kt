@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.engine.docker.resource.network

import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.katan.yoki.*
import org.katan.yoki.engine.docker.*
import org.katan.yoki.resource.network.*
import kotlin.test.*

class NetworkResourceIT {

    companion object {
        const val NETWORK_NAME = "yoki"
    }

    @Test
    fun `create network`() = runTest {
        val client = createTestYoki()
        val createdNetwork = client.networks.create { name = NETWORK_NAME }
        val inspectedNetwork = client.networks.inspect(createdNetwork.id)
        assertEquals(createdNetwork.id, inspectedNetwork.id)

        // cleanup
        client.networks.remove(inspectedNetwork.id)
    }

    @Test
    fun `remove network`() = runTest {
        val client = createTestYoki()
        val network = client.networks.create { name = NETWORK_NAME }
        assertTrue(client.networks.list().any { it.id == network.id })

        client.networks.remove(network.id)
        assertTrue(client.networks.list().none { it.id == network.id })
    }

    @Test
    fun `list networks`() = runTest {
        val client = createTestYoki()

        // the list of networks will never be empty because Docker
        // has predefined networks that cannot be removed
        assertFalse(client.networks.list().isEmpty())
    }

    @Test
    fun `prune networks`() = runTest {
        val client = createTestYoki()
        val oldCount = client.networks.list().size
        val newCount = 5
        repeat(newCount) {
            client.networks.create { name = "$NETWORK_NAME-$it" }
        }

        // check for >= because docker can have default networks defined
        assertEquals(client.networks.list().size, oldCount + newCount)

        client.networks.prune()
        assertEquals(client.networks.list().size, oldCount)
    }
}
