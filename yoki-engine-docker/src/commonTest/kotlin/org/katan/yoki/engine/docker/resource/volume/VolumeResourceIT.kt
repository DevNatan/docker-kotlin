@file:OptIn(ExperimentalCoroutinesApi::class)

package org.katan.yoki.engine.docker.resource.volume

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.engine.docker.createTestYoki
import org.katan.yoki.engine.docker.withVolume
import org.katan.yoki.resource.volume.create
import org.katan.yoki.volumes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VolumeResourceIT {

    companion object {
        const val VOLUME_NAME = "yoki"
    }

    @Test
    fun `create volume`() = runTest {
        val client = createTestYoki()
        client.withVolume { volume ->
            val inspection = client.volumes.inspect(volume.name)
            assertEquals(volume.name, inspection.name)
        }
    }

    @Test
    fun `remove volume`() = runTest {
        val client = createTestYoki()
        val volumes = client.volumes.create { name = VOLUME_NAME }
        assertTrue(client.volumes.list().volumes.any { it.name == volumes.name })

        client.volumes.remove(volumes.name)
        assertTrue(client.volumes.list().volumes.none { it.name == volumes.name })
    }

    @Test
    fun `list volumes`() = runTest {
        val client = createTestYoki()
        val volumes = client.volumes.list().volumes
        assertTrue("Volumes must be empty, given: $volumes") { volumes.isEmpty() }
    }

    @Test
    fun `prune volume`() = runTest {
        val client = createTestYoki()
        val oldCount = client.volumes.list().volumes.size
        val newCount = 5
        repeat(newCount) {
            client.volumes.create()
        }

        assertEquals(client.volumes.list().volumes.size, oldCount + newCount)

        client.volumes.prune()
        assertEquals(client.volumes.list().volumes.size, oldCount)
    }
}
