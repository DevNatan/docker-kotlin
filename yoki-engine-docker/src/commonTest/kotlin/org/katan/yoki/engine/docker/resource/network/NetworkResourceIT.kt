@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.engine.docker.resource.network

import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.katan.yoki.engine.docker.*
import kotlin.test.*

class NetworkResourceIT {

    companion object {
        const val NETWORK_NAME = "yoki"
    }

    @Test
    fun `create network`() = runTest {
        val client = createYoki()
        val createdNetwork = client.networks.create { name = NETWORK_NAME }
        assertNotNull(createdNetwork)

        val inspectedNetwork = client.networks.inspect(createdNetwork.id)
        assertNotNull(inspectedNetwork)
        assertEquals(createdNetwork, inspectedNetwork)
    }

    @Test
    fun `remove network`() = runTest {
        val client = createYoki()
        val network = client.networks.create { name = NETWORK_NAME }
        assertNotNull(network)
        assertFalse(client.networks.list().isEmpty())

        client.networks.remove(network.id)
        assertTrue(client.networks.list().isEmpty())
    }

    @Test
    fun `list networks`() = runTest {
        val client = createYoki()
        assertTrue(client.networks.list().isEmpty())

        val network = client.networks.create {
            name = NETWORK_NAME
        }
        assertNotNull(network)
        assertFalse(client.networks.list().isEmpty())

        // cleanup for other tests
        client.networks.remove(network.id)
        assertTrue(client.networks.list().isEmpty())
    }

    @Test
    fun `prune networks`() = runTest {
        val client = createYoki()
        val count = 5
        repeat(count) {
            client.networks.create { name = "$NETWORK_NAME-$it" }
        }

        // check for >= because docker can have default networks defined
        assertTrue(client.networks.list().size >= count)

        client.networks.prune()
        assertTrue(client.networks.list().isEmpty())
    }

}