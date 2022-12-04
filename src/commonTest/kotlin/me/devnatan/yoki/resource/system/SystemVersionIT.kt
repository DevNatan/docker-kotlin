package me.devnatan.yoki.resource.system

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.devnatan.yoki.YokiResourceException
import me.devnatan.yoki.resource.ResourceIT
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
