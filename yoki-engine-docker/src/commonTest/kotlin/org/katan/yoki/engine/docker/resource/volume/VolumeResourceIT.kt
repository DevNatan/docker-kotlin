@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.engine.docker.resource.volume

import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.katan.yoki.engine.docker.*
import kotlin.test.*

class VolumeResourceIT {

    companion object {
        const val VOLUME_NAME = "yoki"
    }

    @Test
    fun `create volume`() = runTest {
        val client = createYoki()
        val createdVolume = client.volumes.create { name = VOLUME_NAME }
        val inspectedVolume = client.volumes.inspect(createdVolume.name)
        assertEquals(createdVolume.name, inspectedVolume.name)

        // cleanup
        client.volumes.remove(inspectedVolume.name)
    }

    @Test
    fun `remove volume`() = runTest {
        val client = createYoki()
        val volumes = client.volumes.create { name = VOLUME_NAME }
        assertTrue(client.volumes.list().volumes.any { it.name == volumes.name })

        client.volumes.remove(volumes.name)
        assertTrue(client.volumes.list().volumes.none { it.name == volumes.name })
    }

    @Test
    fun `list volume`() = runTest {
        val client = createYoki()

        assertFalse(client.volumes.list().volumes.isEmpty())
    }

    @Test
    fun `prune volume`() = runTest {
        val client = createYoki()
        val oldCount = client.volumes.list().volumes.size
        val newCount = 5
        repeat(newCount) {
            client.volumes.create { name = "$VOLUME_NAME-$it" }
        }

        assertEquals(client.volumes.list().volumes.size, oldCount + newCount)

        client.volumes.prune()
        assertEquals(client.volumes.list().volumes.size, oldCount)
    }

}
