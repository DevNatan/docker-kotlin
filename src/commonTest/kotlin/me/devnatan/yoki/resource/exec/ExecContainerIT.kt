package me.devnatan.yoki.resource.exec

import kotlinx.coroutines.test.runTest
import me.devnatan.yoki.resource.ResourceIT
import me.devnatan.yoki.withContainer
import kotlin.test.Test
import kotlin.test.assertEquals

class ExecContainerIT : ResourceIT() {

    @Test
    fun `exec a command in a container`() = runTest {
        testClient.withContainer(
            "busybox:latest",
            {
                command = listOf("sleep", "infinity")
            },
        ) { id ->
            testClient.containers.start(id)

            val execId = testClient.exec.create(id) {
                command = listOf("true")
            }

            testClient.exec.start(execId)

            val exec = testClient.exec.inspect(execId)
            assertEquals(exec.exitCode, 0)

            testClient.containers.stop(id)
        }
    }

    @Test
    fun `exec a failing command in a container`() = runTest {
        testClient.withContainer(
            "busybox:latest",
            {
                command = listOf("sleep", "infinity")
            },
        ) { id ->
            testClient.containers.start(id)

            val execId = testClient.exec.create(id) {
                command = listOf("false")
            }

            testClient.exec.start(execId)

            val exec = testClient.exec.inspect(execId)
            assertEquals(exec.exitCode, 1)

            testClient.containers.stop(id)
        }
    }
}
