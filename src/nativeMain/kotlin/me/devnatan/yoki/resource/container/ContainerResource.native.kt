package me.devnatan.yoki.resource.container

import kotlinx.coroutines.flow.Flow
import kotlinx.io.RawSource
import me.devnatan.yoki.models.Frame
import me.devnatan.yoki.models.ResizeTTYOptions
import me.devnatan.yoki.models.container.Container
import me.devnatan.yoki.models.container.ContainerArchiveInfo
import me.devnatan.yoki.models.container.ContainerCreateOptions
import me.devnatan.yoki.models.container.ContainerListOptions
import me.devnatan.yoki.models.container.ContainerPruneFilters
import me.devnatan.yoki.models.container.ContainerPruneResult
import me.devnatan.yoki.models.container.ContainerRemoveOptions
import me.devnatan.yoki.models.container.ContainerSummary
import me.devnatan.yoki.models.container.ContainerWaitResult
import kotlin.time.Duration

public actual class ContainerResource {

    /**
     * Returns a list of all containers.
     *
     * @param options Options to customize the listing result.
     */
    public actual suspend fun list(options: ContainerListOptions): List<ContainerSummary> {
        TODO("Not yet implemented")
    }

    /**
     * Creates a new container.
     *
     * @param options Options to customize the container creation.
     * @throws ImageNotFoundException If the image specified does not exist or isn't pulled.
     * @throws ContainerAlreadyExistsException If a container with the same name already exists.
     */
    public actual suspend fun create(options: ContainerCreateOptions): String {
        TODO("Not yet implemented")
    }

    /**
     * Removes a container.
     *
     * @param container The container id to remove.
     * @param options Removal options.
     * @throws ContainerNotFoundException If the container is not found for the specified id.
     * @throws ContainerRemoveConflictException When trying to remove an active container without the `force` option.
     */
    public actual suspend fun remove(
        container: String,
        options: ContainerRemoveOptions,
    ) {
    }

    /**
     * Returns low-level information about a container.
     *
     * @param container ID or name of the container.
     * @param size Should return the size of container as fields `SizeRw` and `SizeRootFs`
     */
    public actual suspend fun inspect(
        container: String,
        size: Boolean,
    ): Container {
        TODO("Not yet implemented")
    }

    /**
     * Starts a container.
     *
     * @param container The container id to be started.
     * @param detachKeys The key sequence for detaching a container.
     * @throws ContainerAlreadyStartedException If the container was already started.
     * @throws ContainerNotFoundException If container was not found.
     */
    public actual suspend fun start(container: String, detachKeys: String?) {
    }

    /**
     * Stops a container.
     *
     * @param container The container id to stop.
     * @param timeout Duration to wait before killing the container.
     */
    public actual suspend fun stop(container: String, timeout: Duration?) {
    }

    /**
     * Restarts a container.
     *
     * @param container The container id to restart.
     * @param timeout Duration to wait before killing the container.
     */
    public actual suspend fun restart(container: String, timeout: Duration?) {
    }

    /**
     * Kills a container.
     *
     * @param container The container id to kill.
     * @param signal Signal to send for container to be killed, Docker's default is "SIGKILL".
     */
    public actual suspend fun kill(container: String, signal: String?) {
    }

    /**
     * Renames a container.
     *
     * @param container The container id to rename.
     * @param newName The new container name.
     */
    public actual suspend fun rename(container: String, newName: String) {
    }

    /**
     * Pauses a container.
     *
     * @param container The container id to pause.
     * @see unpause
     */
    public actual suspend fun pause(container: String) {
    }

    /**
     * Resumes a container which has been paused.
     *
     * @param container The container id to unpause.
     * @see pause
     */
    public actual suspend fun unpause(container: String) {
    }

    /**
     * Resizes the TTY for a container.
     *
     * @param container The container id to resize.
     * @param options Resize options like width and height.
     * @throws ContainerNotFoundException If the container is not found.
     * @throws YokiResponseException If the container cannot be resized or if an error occurs in the request.
     */
    public actual suspend fun resizeTTY(container: String, options: ResizeTTYOptions) {
    }

    public actual fun attach(container: String): Flow<Frame> {
        TODO("Not yet implemented")
    }

    public actual suspend fun wait(
        container: String,
        condition: String?,
    ): ContainerWaitResult {
        TODO("Not yet implemented")
    }

    public actual suspend fun prune(filters: ContainerPruneFilters): ContainerPruneResult {
        TODO("Not yet implemented")
    }

    /**
     * Retrieves information about files of a container file system.
     *
     * @param container The container id.
     * @param path The path to the file or directory inside the container file system.
     */
    public actual suspend fun archive(
        container: String,
        path: String,
    ): ContainerArchiveInfo {
        TODO("Not yet implemented")
    }

    /**
     * Downloads files from a container file system.
     *
     * @param container The container id.
     * @param remotePath The path to the file or directory inside the container file system.
     */
    public actual suspend fun downloadArchive(container: String, remotePath: String): RawSource {
        TODO("Not yet implemented")
    }

    /**
     * Uploads files into a container file system.
     *
     * @param container The container id.
     * @param inputPath Path to the file that will be uploaded.
     * @param remotePath Path to the file or directory inside the container file system.
     */
    public actual suspend fun uploadArchive(
        container: String,
        inputPath: String,
        remotePath: String,
    ) {
    }
}
