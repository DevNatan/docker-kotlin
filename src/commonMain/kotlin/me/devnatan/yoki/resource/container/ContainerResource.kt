package me.devnatan.yoki.resource.container

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.NotModified
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.devnatan.yoki.Closeable
import me.devnatan.yoki.YokiResponseException
import me.devnatan.yoki.io.InternalYokiFlow
import me.devnatan.yoki.io.YokiFlow
import me.devnatan.yoki.io.requestCatching
import me.devnatan.yoki.logging.Logger
import me.devnatan.yoki.models.Frame
import me.devnatan.yoki.models.IdOnlyResponse
import me.devnatan.yoki.models.ResizeTTYOptions
import me.devnatan.yoki.models.Stream
import me.devnatan.yoki.models.container.Container
import me.devnatan.yoki.models.container.ContainerCreateOptions
import me.devnatan.yoki.models.container.ContainerCreateResult
import me.devnatan.yoki.models.container.ContainerListOptions
import me.devnatan.yoki.models.container.ContainerPruneFilters
import me.devnatan.yoki.models.container.ContainerPruneResult
import me.devnatan.yoki.models.container.ContainerRemoveOptions
import me.devnatan.yoki.models.container.ContainerSummary
import me.devnatan.yoki.models.container.ContainerWaitResult
import me.devnatan.yoki.models.exec.ExecCreateOptions
import me.devnatan.yoki.resource.image.ImageNotFoundException
import java.util.concurrent.CompletableFuture
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

public class ContainerResource internal constructor(
    internal val httpClient: HttpClient,
    private val json: Json,
    private val logger: Logger,
    private val coroutineScope: CoroutineScope,
) {

    internal companion object {
        internal const val BASE_PATH = "/containers"
        private val LINE_BREAK_REGEX: Regex = Regex("\\r\\n|\\n|\\r")
    }

    /**
     * Returns a list of all containers.
     *
     * @param options Options to customize the listing result.
     */
    @JvmSynthetic
    public suspend fun list(options: ContainerListOptions = ContainerListOptions(all = true)): List<ContainerSummary> =
        requestCatching {
            httpClient.get("$BASE_PATH/json") {
                parameter("all", options.all)
                parameter("limit", options.limit)
                parameter("size", options.size)
                parameter("filters", options.filters?.let(json::encodeToString))
            }
        }.body()

    /**
     * Returns a list of all containers.
     *
     * @param options Options to customize the listing result.
     */
    @JvmOverloads
    public fun listAsync(options: ContainerListOptions = ContainerListOptions(all = true)): CompletableFuture<List<ContainerSummary>> = coroutineScope.async { list(options) }.asCompletableFuture()

    /**
     * Creates a new container.
     *
     * @param options Options to customize the container creation.
     * @throws ImageNotFoundException If the image specified does not exist or isn't pulled.
     * @throws ContainerAlreadyExistsException If a container with the same name already exists.
     */
    public suspend fun create(options: ContainerCreateOptions): String {
        requireNotNull(options.image) { "Container image is required" }

        val result = requestCatching(
            NotFound to { exception -> ImageNotFoundException(exception, options.image.orEmpty()) },
            Conflict to { exception ->
                ContainerAlreadyExistsException(
                    exception,
                    options.name.orEmpty(),
                )
            },
        ) {
            httpClient.post("$BASE_PATH/create") {
                parameter("name", options.name)
                setBody(options)
            }
        }.body<ContainerCreateResult>()

        result.warnings.forEach(logger::warn)
        return result.id
    }

    /**
     * Creates a new container.
     *
     * @param options Options to customize the container creation.
     * @throws ImageNotFoundException If the image specified does not exist or isn't pulled.
     * @throws ContainerAlreadyExistsException If a container with the same name already exists.
     */
    public fun createAsync(options: ContainerCreateOptions): CompletableFuture<String> = coroutineScope.async {
        create(options)
    }.asCompletableFuture()

    /**
     * Starts a container.
     *
     * @param id The container id to be started.
     * @param detachKeys The key sequence for detaching a container.
     * @throws ContainerAlreadyStartedException If the container was already started.
     * @throws ContainerNotFoundException If container was not found.
     */
    @JvmSynthetic
    public suspend fun start(id: String, detachKeys: String? = null) {
        requestCatching(
            NotModified to { exception -> ContainerAlreadyStartedException(exception, id) },
            NotFound to { exception -> ContainerNotFoundException(exception, id) },
        ) {
            httpClient.post("$BASE_PATH/$id/start") {
                parameter("detachKeys", detachKeys)
            }
        }
    }

    /**
     * Starts a container.
     *
     * @param id The container id to be started.
     * @param detachKeys The key sequence for detaching a container.
     * @throws ContainerAlreadyStartedException If the container was already started.
     * @throws ContainerNotFoundException If container was not found.
     */
    @JvmOverloads
    public fun startAsync(id: String, detachKeys: String? = null): CompletableFuture<Unit> = coroutineScope.async {
        start(id, detachKeys)
    }.asCompletableFuture()

    /**
     * Stops a container.
     *
     * @param id The container id to stop.
     * @param timeout Duration to wait before killing the container.
     */
    @JvmSynthetic
    public suspend fun stop(id: String, timeout: Duration? = null) {
        requestCatching(
            NotModified to { exception -> ContainerAlreadyStoppedException(exception, id) },
            NotFound to { exception -> ContainerNotFoundException(exception, id) },
        ) {
            httpClient.post("$BASE_PATH/$id/stop") {
                parameter("t", timeout?.inWholeSeconds)
            }
        }
    }

    /**
     * Stops a container.
     *
     * @param id The container id to stop.
     */
    public fun stopAsync(id: String): CompletableFuture<Unit> = coroutineScope.async {
        stop(id, timeout = null)
    }.asCompletableFuture()

    /**
     * Stops a container.
     *
     * @param id The container id to stop.
     * @param timeoutInSeconds Duration in seconds to wait before killing the container.
     */
    public fun stopAsync(id: String, timeoutInSeconds: Int): CompletableFuture<Unit> = coroutineScope.async {
        stop(id, timeoutInSeconds.toDuration(DurationUnit.SECONDS))
    }.asCompletableFuture()

    /**
     * Restarts a container.
     *
     * @param id The container id to restart.
     * @param timeout Duration to wait before killing the container.
     */
    @JvmSynthetic
    public suspend fun restart(id: String, timeout: Duration? = null) {
        requestCatching(
            NotFound to { exception -> ContainerNotFoundException(exception, id) },
        ) {
            httpClient.post("$BASE_PATH/$id/restart") {
                parameter("t", timeout)
            }
        }
    }

    /**
     * Restarts a container.
     *
     * @param id The container id to restart.
     */
    public fun restartAsync(id: String): CompletableFuture<Unit> = coroutineScope.async {
        restart(id, timeout = null)
    }.asCompletableFuture()

    /**
     * Restarts a container.
     *
     * @param id The container id to restart.
     * @param timeoutInSeconds Duration in seconds to wait before killing the container.
     */
    public fun restartAsync(id: String, timeoutInSeconds: Int): CompletableFuture<Unit> = coroutineScope.async {
        restart(id, timeoutInSeconds.toDuration(DurationUnit.SECONDS))
    }.asCompletableFuture()

    /**
     * Kills a container.
     *
     * @param id The container id to kill.
     * @param signal Signal to send for container to be killed, Docker's default is "SIGKILL".
     */
    @JvmSynthetic
    public suspend fun kill(id: String, signal: String? = null) {
        requestCatching(
            NotFound to { exception -> ContainerNotFoundException(exception, id) },
            Conflict to { exception -> ContainerNotRunningException(exception, id) },
        ) {
            httpClient.post("$BASE_PATH/$id/kill") {
                parameter("signal", signal)
            }
        }
    }

    /**
     * Kills a container.
     *
     *
     * @param id The container id to kill.
     * @param signal Signal to send for container to be killed, Docker's default is "SIGKILL".
     */
    @JvmOverloads
    public fun killAsync(id: String, signal: String? = null): CompletableFuture<Unit> = coroutineScope.async {
        kill(id, signal)
    }.asCompletableFuture()

    /**
     * Renames a container.
     *
     * @param id The container id to rename.
     * @param newName The new container name.
     */
    @JvmSynthetic
    public suspend fun rename(id: String, newName: String) {
        requestCatching(
            NotFound to { exception -> ContainerNotFoundException(exception, id) },
            Conflict to { exception -> ContainerRenameConflictException(exception, id, newName) },
        ) {
            httpClient.post("$BASE_PATH/$id/rename") {
                parameter("name", newName)
            }
        }
    }

    /**
     * Renames a container.
     *
     * @param id The container id to rename.
     * @param newName The new container name.
     */
    public fun renameAsync(id: String, newName: String): CompletableFuture<Unit> = coroutineScope.async {
        rename(id, newName)
    }.asCompletableFuture()

    /**
     * Pauses a container.
     *
     * @param id The container id to pause.
     * @see unpause
     */
    @JvmSynthetic
    public suspend fun pause(id: String): Unit = requestCatching(
        NotFound to { exception -> ContainerNotFoundException(exception, id) },
    ) {
        httpClient.post("$BASE_PATH/$id/pause")
    }

    /**
     * Pauses a container.
     *
     * @param id The container id to pause.
     * @see unpause
     */
    public fun pauseAsync(id: String): CompletableFuture<Unit> = coroutineScope.async {
        pause(id)
    }.asCompletableFuture()

    /**
     * Resumes a container which has been paused.
     *
     * @param id The container id to unpause.
     * @see pause
     */
    @JvmSynthetic
    public suspend fun unpause(id: String): Unit = requestCatching(
        NotFound to { exception -> ContainerNotFoundException(exception, id) },
    ) {
        httpClient.post("$BASE_PATH/$id/unpause")
    }

    /**
     * Resumes a container which has been paused.
     *
     * @param id The container id to unpause.
     * @see pause
     */
    public fun unpauseAsync(id: String): CompletableFuture<Unit> = coroutineScope.async {
        unpause(id)
    }.asCompletableFuture()

    /**
     * Removes a container.
     *
     * @param id The container id to remove.
     * @param options Removal options.
     * @throws ContainerNotFoundException If the container is not found for the specified id.
     * @throws ContainerRemoveConflictException When trying to remove an active container without the `force` option.
     */
    @JvmSynthetic
    public suspend fun remove(id: String, options: ContainerRemoveOptions = ContainerRemoveOptions()): Unit =
        requestCatching(
            NotFound to { ContainerNotFoundException(it, id) },
            Conflict to { ContainerRemoveConflictException(it, id) },
        ) {
            httpClient.delete("$BASE_PATH/$id") {
                parameter("v", options.removeAnonymousVolumes)
                parameter("force", options.force)
                parameter("link", options.unlink)
            }
        }

    /**
     * Removes a container.
     *
     * @param id The container id to remove.
     * @param options Removal options.
     * @throws ContainerNotFoundException If the container is not found for the specified id.
     * @throws ContainerRemoveConflictException When trying to remove an active container without the `force` option.
     */
    @JvmOverloads
    public fun removeAsync(id: String, options: ContainerRemoveOptions = ContainerRemoveOptions()): CompletableFuture<Unit> =
        coroutineScope.async {
            remove(id, options)
        }.asCompletableFuture()

    /**
     * Returns low-level information about a container.
     *
     * @param id ID or name of the container.
     * @param size Should return the size of container as fields `SizeRw` and `SizeRootFs`
     */
    @JvmSynthetic
    public suspend fun inspect(id: String, size: Boolean = false): Container = requestCatching(
        NotFound to { ContainerNotFoundException(it, id) },
    ) {
        httpClient.get("$BASE_PATH/$id/json") {
            parameter("size", size)
        }
    }.body()

    /**
     * Returns low-level information about a container.
     *
     * @param id ID or name of the container.
     * @param size Should return the size of container as fields `SizeRw` and `SizeRootFs`
     */
    @JvmOverloads
    public fun inspectAsync(id: String, size: Boolean = false): CompletableFuture<Container> = coroutineScope.async {
        inspect(id, size)
    }.asCompletableFuture()

    public fun logs(id: String, callback: YokiFlow<Frame>): Closeable = InternalYokiFlow().also { flow ->
        flow.start(flow = logs(id), callback = callback)
    }

    @JvmSynthetic
    public suspend fun prune(filters: ContainerPruneFilters = ContainerPruneFilters()): ContainerPruneResult =
        httpClient.post("$BASE_PATH/prune") {
            parameter("filters", json.encodeToString(filters))
        }.body()

    @JvmOverloads
    public fun pruneAsync(filters: ContainerPruneFilters = ContainerPruneFilters()): CompletableFuture<ContainerPruneResult> =
        coroutineScope.async { prune(filters) }.asCompletableFuture()

    @JvmSynthetic
    public suspend fun wait(id: String, condition: String? = null): ContainerWaitResult {
        return httpClient.post("$BASE_PATH/$id/wait") {
            parameter("condition", condition)
        }.body()
    }

    @JvmOverloads
    public fun waitAsync(id: String, condition: String? = null): CompletableFuture<ContainerWaitResult> =
        coroutineScope.async { wait(id, condition) }.asCompletableFuture()

    @JvmSynthetic
    public fun attach(containerIdOrName: String): Flow<Frame> = flow {
        httpClient.preparePost("$BASE_PATH/$containerIdOrName/attach") {
            parameter("stream", "true")
            parameter("stdin", "true")
            parameter("stdout", "true")
            parameter("stderr", "true")
        }.execute { response ->
            val channel = response.body<ByteReadChannel>()
            while (!channel.isClosedForRead) {
                val line = channel.readUTF8Line() ?: break

                // TODO handle stream type
                emit(Frame(line, line.length, Stream.StdOut))
            }
        }
    }

    public fun attach(containerIdOrName: String, callback: YokiFlow<Frame>): Unit = with(InternalYokiFlow()) {
        start(attach(containerIdOrName), callback)
    }

    /**
     * Resizes the TTY for a container.
     *
     * @param container Unique identifier or name of the container.
     * @param options Resize options like width and height.
     * @throws ContainerNotFoundException If the container is not found.
     * @throws YokiResponseException If the container cannot be resized or if an error occurs in the request.
     */
    @JvmSynthetic
    public suspend fun resizeTTY(container: String, options: ResizeTTYOptions = ResizeTTYOptions()): Unit =
        requestCatching(
            NotFound to { exception ->
                ContainerNotFoundException(
                    exception,
                    container,
                )
            },
        ) {
            httpClient.post("$BASE_PATH/$container/resize") {
                setBody(options)
            }
        }

    /**
     * Resizes the TTY for a container.
     *
     * @param container Unique identifier or name of the container.
     * @param options Resize options like width and height.
     * @throws ContainerNotFoundException If the container is not found.
     * @throws YokiResponseException If the container cannot be resized or if an error occurs in the request.
     */
    @JvmOverloads
    public fun resizeTTYAsync(container: String, options: ResizeTTYOptions = ResizeTTYOptions()): CompletableFuture<Unit> = coroutineScope.async {
        resizeTTY(container, options)
    }.asCompletableFuture()

    /**
     * Runs a command inside a running container.
     *
     * @param container Unique identifier or name of the container.
     * @param options Exec instance command options.
     */
    @JvmSynthetic
    public suspend fun exec(container: String, options: ExecCreateOptions = ExecCreateOptions()): String =
        requestCatching(
            NotFound to { exception ->
                ContainerNotFoundException(
                    exception,
                    container,
                )
            },
            Conflict to { exception ->
                ContainerNotRunningException(
                    exception,
                    container,
                )
            },
        ) {
            httpClient.post("$BASE_PATH/$container/exec") {
                setBody(options)
            }
        }.body<IdOnlyResponse>().id

    /**
     * Runs a command inside a running container.
     *
     * @param container Unique identifier or name of the container.
     * @param options Exec instance command options.
     */
    @JvmOverloads
    public fun execAsync(container: String, options: ExecCreateOptions = ExecCreateOptions()): CompletableFuture<String> =
        coroutineScope.async { exec(container, options) }.asCompletableFuture()
}
