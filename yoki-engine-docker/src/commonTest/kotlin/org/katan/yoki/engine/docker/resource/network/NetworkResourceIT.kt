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
        val createdNetwork = client.network.create { name = NETWORK_NAME }
        assertNotNull(createdNetwork)

        val inspectedNetwork = client.network.inspect(createdNetwork.id)
        assertNotNull(inspectedNetwork)
        assertEquals(createdNetwork, inspectedNetwork)
    }

    @Test
    fun `remove network`() = runTest {
        val client = createYoki()
        val network = client.network.create { name = NETWORK_NAME }
        assertNotNull(network)
        assertFalse(client.network.list().isEmpty())

        client.network.remove(network.id)
        assertTrue(client.network.list().isEmpty())
    }

    @Test
    fun `list networks`() = runTest {
        val client = createYoki()
        assertTrue(client.network.list().isEmpty())

        val network = client.network.create {
            name = NETWORK_NAME
        }
        assertNotNull(network)
        assertFalse(client.network.list().isEmpty())

        // cleanup for other tests
        client.network.remove(network.id)
        assertTrue(client.network.list().isEmpty())
    }

    @Test
    fun `prune networks`() = runTest {
        val client = createYoki()
        val count = 5
        repeat(count) {
            client.network.create { name = "$NETWORK_NAME-$it" }
        }

        // check for >= because docker can have default networks defined
        assertTrue(client.network.list().size >= count)

        client.network.prune()
        assertTrue(client.network.list().isEmpty())
    }

}