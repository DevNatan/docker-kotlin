package me.devnatan.yoki.resource.container

import kotlinx.coroutines.flow.Flow
import me.devnatan.yoki.YokiResponseException
import me.devnatan.yoki.models.Frame
import me.devnatan.yoki.models.ResizeTTYOptions
import me.devnatan.yoki.models.container.Container
import me.devnatan.yoki.models.container.ContainerCreateOptions
import me.devnatan.yoki.models.container.ContainerListOptions
import me.devnatan.yoki.models.container.ContainerPruneFilters
import me.devnatan.yoki.models.container.ContainerPruneResult
import me.devnatan.yoki.models.container.ContainerRemoveOptions
import me.devnatan.yoki.models.container.ContainerSummary
import me.devnatan.yoki.models.container.ContainerWaitResult
import me.devnatan.yoki.models.exec.ExecCreateOptions
import me.devnatan.yoki.resource.image.ImageNotFoundException
import kotlin.time.Duration

public expect class ContainerResource {

    /**
     * Returns a list of all containers.
     *
     * @param options Options to customize the listing result.
     */
    public suspend fun list(options: ContainerListOptions = ContainerListOptions(all = true)): List<ContainerSummary>

    /**
     * Creates a new container.
     *
     * @param options Options to customize the container creation.
     * @throws ImageNotFoundException If the image specified does not exist or isn't pulled.
     * @throws ContainerAlreadyExistsException If a container with the same name already exists.
     */
    public suspend fun create(options: ContainerCreateOptions): String

    /**
     * Removes a container.
     *
     * @param container The container id to remove.
     * @param options Removal options.
     * @throws ContainerNotFoundException If the container is not found for the specified id.
     * @throws ContainerRemoveConflictException When trying to remove an active container without the `force` option.
     */
    public suspend fun remove(container: String, options: ContainerRemoveOptions = ContainerRemoveOptions())

    /**
     * Returns low-level information about a container.
     *
     * @param container ID or name of the container.
     * @param size Should return the size of container as fields `SizeRw` and `SizeRootFs`
     */
    public suspend fun inspect(container: String, size: Boolean = false): Container

    /**
     * Starts a container.
     *
     * @param container The container id to be started.
     * @param detachKeys The key sequence for detaching a container.
     * @throws ContainerAlreadyStartedException If the container was already started.
     * @throws ContainerNotFoundException If container was not found.
     */
    public suspend fun start(container: String, detachKeys: String? = null)

    /**
     * Stops a container.
     *
     * @param container The container id to stop.
     * @param timeout Duration to wait before killing the container.
     */
    public suspend fun stop(container: String, timeout: Duration? = null)

    /**
     * Restarts a container.
     *
     * @param container The container id to restart.
     * @param timeout Duration to wait before killing the container.
     */
    public suspend fun restart(container: String, timeout: Duration? = null)

    /**
     * Kills a container.
     *
     * @param container The container id to kill.
     * @param signal Signal to send for container to be killed, Docker's default is "SIGKILL".
     */
    public suspend fun kill(container: String, signal: String? = null)

    /**
     * Renames a container.
     *
     * @param container The container id to rename.
     * @param newName The new container name.
     */
    public suspend fun rename(container: String, newName: String)

    /**
     * Pauses a container.
     *
     * @param container The container id to pause.
     * @see unpause
     */
    public suspend fun pause(container: String)

    /**
     * Resumes a container which has been paused.
     *
     * @param container The container id to unpause.
     * @see pause
     */
    public suspend fun unpause(container: String)

    /**
     * Resizes the TTY for a container.
     *
     * @param container The container id to resize.
     * @param options Resize options like width and height.
     * @throws ContainerNotFoundException If the container is not found.
     * @throws YokiResponseException If the container cannot be resized or if an error occurs in the request.
     */
    public suspend fun resizeTTY(container: String, options: ResizeTTYOptions = ResizeTTYOptions())

    /**
     * Runs a command inside a running container.
     *
     * @param container The container id to execute the command.
     * @param options Exec instance command options.
     */
    public suspend fun exec(container: String, options: ExecCreateOptions = ExecCreateOptions()): String

    // TODO documentation
    public fun attach(container: String): Flow<Frame>

    // TODO documentation
    public suspend fun wait(container: String, condition: String? = null): ContainerWaitResult

    // TODO documentation
    public suspend fun prune(filters: ContainerPruneFilters = ContainerPruneFilters()): ContainerPruneResult

    /**
     * Downloads files from a container file system.
     *
     * @param container The container id.
     */
    public suspend fun downloadArchive(container: String)

    /**
     * Uploads files into a container file system.
     *
     * @param container The container id.
     */
    public suspend fun uploadArchive(container: String)
}
