package org.katan.yoki.engine.docker

import kotlinx.coroutines.flow.collect
import org.katan.yoki.Docker
import org.katan.yoki.DockerEngineConfig
import org.katan.yoki.Yoki
import org.katan.yoki.YokiConfig
import org.katan.yoki.images
import kotlin.test.fail

/**
 * Creates a new Yoki instance for testing.
 * @param block The client configuration factory.
 */
fun createTestYoki(block: YokiConfig<DockerEngineConfig>.() -> Unit = {}): Yoki {
    return Yoki(Docker) { apply(block) }
}

suspend fun Yoki.withImage(imageName: String, block: suspend (String) -> Unit) {
    try {
        images.pull(imageName).collect()
    } catch (e: Throwable) {
        fail("Failed to pull image", e)
    }

    block(imageName)
    images.remove(imageName, force = true)
}
