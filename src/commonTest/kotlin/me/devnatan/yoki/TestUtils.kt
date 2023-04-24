package me.devnatan.yoki

import kotlinx.coroutines.flow.collect
import me.devnatan.yoki.models.container.ContainerCreateOptions
import me.devnatan.yoki.models.volume.Volume
import me.devnatan.yoki.models.volume.VolumeCreateOptions
import me.devnatan.yoki.resource.container.create
import me.devnatan.yoki.resource.container.remove
import me.devnatan.yoki.resource.volume.create
import me.devnatan.yoki.resource.volume.remove
import kotlin.test.fail

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
    block: suspend (String) -> R,
) = withImage(image) { imageTag ->
    try {
        val id = containers.create {
            this.image = imageTag
            apply(options)
        }
        block(id)
        containers.remove(id) {
            force = true
            removeAnonymousVolumes = true
        }
    } catch (e: Throwable) {
        fail("Failed to create container", e)
    }
}

suspend fun <R> Yoki.withVolume(
    config: VolumeCreateOptions.() -> Unit = {},
    block: suspend (Volume) -> R,
) {
    try {
        val volume = volumes.create(config)
        block(volume)
        volumes.remove(volume.name) {
            force = true
        }
    } catch (e: Throwable) {
        fail("Failed to create volume", e)
    }
}

/**
 * Make a container started forever.
 */
fun ContainerCreateOptions.keepStartedForever() {
    openStdin = true
    tty = true
}
