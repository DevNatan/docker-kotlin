package org.katan.yoki.engine.docker

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestScope
import org.katan.yoki.Docker
import org.katan.yoki.DockerEngineConfig
import org.katan.yoki.Yoki
import org.katan.yoki.YokiConfig
import org.katan.yoki.containers
import org.katan.yoki.images
import org.katan.yoki.model.volume.Volume
import org.katan.yoki.resource.container.ContainerCreateOptions
import org.katan.yoki.resource.container.create
import org.katan.yoki.resource.volume.VolumeConfig
import org.katan.yoki.resource.volume.create
import org.katan.yoki.volumes
import kotlin.test.fail

internal const val TEST_CONTAINER_NAME = "yoki-test"

/**
 * Creates a new Yoki instance for testing.
 * @param block The client configuration factory.
 */
fun createTestYoki(block: YokiConfig<DockerEngineConfig>.() -> Unit = {}): Yoki {
    return Yoki(Docker) { apply(block) }
}

suspend fun <R> Yoki.withImage(imageName: String, block: suspend (String) -> R): R {
    try {
        images.pull(imageName).collect()
    } catch (e: Throwable) {
        fail("Failed to pull image", e)
    }

    try {
        return block(imageName)
    } finally {
        images.remove(imageName, force = true)
    }
}

suspend fun <R> Yoki.withContainer(
    image: String,
    options: ContainerCreateOptions.() -> Unit = {},
    block: suspend (String) -> R
) = withImage(image) { imageTag ->
    try {
        val id = containers.create {
            this.image = imageTag
            apply(options)
        }
        block(id)
        containers.remove(id)
    } catch (e: Throwable) {
        fail("Failed to create container", e)
    }
}

suspend fun <R> Yoki.withVolume(
    config: VolumeConfig.() -> Unit = {},
    block: suspend (Volume) -> R
) {
    try {
        val volume = volumes.create(config)
        block(volume)
        volumes.remove(volume.name)
    } catch (e: Throwable) {
        fail("Failed to create volume", e)
    }
}

/**
 * Creates a simple test container with no name and "hello-world" image.
 */
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun TestScope.createTestContainer(client: Yoki, options: ContainerCreateOptions.() -> Unit = {}): String {
    return client.withImage("busybox:latest") { pulledImageTag ->
        runCatching {
            client.containers.create {
                name = TEST_CONTAINER_NAME
                image = pulledImageTag
                apply(options)
            }
        }.onFailure { fail("Failed to create test container: $it", it) }.getOrThrow()
    }
}

/**
 * Make a container started forever.
 */
fun ContainerCreateOptions.keepStartedForever() {
    openStdin = true
    tty = true
}
