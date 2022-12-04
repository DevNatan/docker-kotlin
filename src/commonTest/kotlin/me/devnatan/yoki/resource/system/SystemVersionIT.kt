package org.katan.yoki.resource.system

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.yoki.YokiResourceException
import org.katan.yoki.resource.ResourceIT
import kotlin.test.Test
import kotlin.test.fail

@ExperimentalCoroutinesApi
class SystemVersionIT : ResourceIT() {

    @Test
    fun `fetch system version`() = runTest {
        try {
            testClient.system.version()
        } catch (e: YokiResourceException) {
            fail("Failed to fetch information about system version.", e)
        }
    }
}
