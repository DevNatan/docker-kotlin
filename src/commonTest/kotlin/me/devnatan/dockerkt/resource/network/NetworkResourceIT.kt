@file:OptIn(ExperimentalCoroutinesApi::class)

package me.devnatan.dockerkt.resource.network

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.devnatan.dockerkt.resource.ResourceIT
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NetworkResourceIT : ResourceIT() {
    companion object {
        const val NETWORK_NAME = "docker-kotlin"
    }

    @Test
    fun `create network`() =
        runTest {
            val createdNetwork = testClient.networks.create { name = NETWORK_NAME }
            val inspectedNetwork = testClient.networks.inspect(createdNetwork.id)
            assertEquals(createdNetwork.id, inspectedNetwork.id)

            // cleanup
            testClient.networks.remove(inspectedNetwork.id)
        }

    @Test
    fun `remove network`() =
        runTest {
            val network = testClient.networks.create { name = NETWORK_NAME }
            assertTrue(testClient.networks.list().any { it.id == network.id })

            testClient.networks.remove(network.id)
            assertTrue(testClient.networks.list().none { it.id == network.id })
        }

    @Test
    fun `list networks`() =
        runTest {
            // the list of networks will never be empty because Docker
            // has predefined networks that cannot be removed
            assertFalse(testClient.networks.list().isEmpty())
        }

    @Test
    fun `prune networks`() =
        runTest {
            val oldCount = testClient.networks.list().size
            val newCount = 5
            repeat(newCount) {
                testClient.networks.create { name = "$NETWORK_NAME-$it" }
            }

            // check for >= because docker can have default networks defined
            assertEquals(testClient.networks.list().size, oldCount + newCount)

            testClient.networks.prune()
            assertEquals(testClient.networks.list().size, oldCount)
        }
}
