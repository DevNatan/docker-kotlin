package org.katan.yoki.resource.system

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.YokiResourceException
import org.katan.yoki.createTestYoki
import kotlin.test.Test
import kotlin.test.fail

@ExperimentalCoroutinesApi
class SystemVersionIT {

    @Test
    fun `fetch system version`() = runTest {
        val client = createTestYoki()
        try {
            client.system.version()
        } catch (e: YokiResourceException) {
            fail("Failed to fetch information about system version.", e)
        }
    }
}
