package org.katan.engine.container

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.katan.engine.Engine
import org.katan.engine.Podman
import kotlin.test.Test

/**
 * @see PodmanContainerFactory
 */
@ExperimentalCoroutinesApi
class PodmanContainerFactoryTests {

    private val podman: Engine = Podman()

    @Test
    fun testContainerCreate() = runTest {
        podman.containerFactory.create(PodmanContainer("busybox", "yoki-test"))
    }

}