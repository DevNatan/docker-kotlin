package me.devnatan.yoki.resource.container

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.prepareGet
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.NotModified
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.ByteOrder
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.devnatan.yoki.Closeable
import me.devnatan.yoki.YokiResponseException
import me.devnatan.yoki.io.InternalYokiFlow
import me.devnatan.yoki.io.YokiFlow
import me.devnatan.yoki.logging.Logger
import me.devnatan.yoki.models.Frame
import me.devnatan.yoki.models.IdOnlyResponse
import me.devnatan.yoki.models.ResizeTTYOptions
import me.devnatan.yoki.models.Stream
import me.devnatan.yoki.models.container.Container
import me.devnatan.yoki.models.container.ContainerCreateOptions
import me.devnatan.yoki.models.container.ContainerCreateResult
import me.devnatan.yoki.models.container.ContainerListOptions
import me.devnatan.yoki.models.container.ContainerLogsOptions
import me.devnatan.yoki.models.container.ContainerPruneFilters
import me.devnatan.yoki.models.container.ContainerPruneResult
import me.devnatan.yoki.models.container.ContainerRemoveOptions
import me.devnatan.yoki.models.container.ContainerSummary
import me.devnatan.yoki.models.container.ContainerWaitResult
import me.devnatan.yoki.models.exec.ExecCreateOptions
import me.devnatan.yoki.io.requestCatching
import me.devnatan.yoki.resource.image.ImageNotFoundException
import kotlin.time.Duration

public class ContainerResource internal constructor(
    internal val httpClient: HttpClient,
    private val json: Json,
    private val logger: Logger,
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
    public suspend fun list(options: ContainerListOptions = ContainerListOptions(all = true)): List<ContainerSummary> {
        return requestCatching {
            httpClient.get("$BASE_PATH/json") {
                parameter("all", options.all)
                parameter("limit", options.limit)
                parameter("size", options.size)
                parameter("filters", options.filters?.let(json::encodeToString))
            }
        }.body()
    }

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
     * Starts a container.
     *
     * @param id The container id to be started.
     * @param detachKeys The key sequence for detaching a container.
     * @throws ContainerAlreadyStartedException If the container was already started.
     * @throws ContainerNotFoundException If container was not found.
     */
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
     * Stops a container.
     *
     * @param id The container id to stop.
     * @param timeout Duration to wait before killing the container.
     */
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
     * Restarts a container.
     *
     * @param id The container id to restart.
     * @param timeout Duration to wait before killing the container.
     */
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
     * Kills a container.
     *
     * @param id The container id to kille.
     * @param signal Signal to send for container to be killed, default is "SIGKILL"
     */
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
     * Renames a container.
     *
     * @param id The container id to rename.
     * @param newName The new container name.
     */
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
     * Pauses a container.
     *
     * @param id The container id to pause.
     * @see unpause
     */
    public suspend fun pause(id: String) {
        requestCatching(
            NotFound to { exception -> ContainerNotFoundException(exception, id) },
        ) {
            httpClient.post("$BASE_PATH/$id/pause")
        }
    }

    /**
     * Resumes a container which has been paused.
     *
     * @param id The container id to unpause.
     * @see pause
     */
    public suspend fun unpause(id: String) {
        requestCatching(
            NotFound to { exception -> ContainerNotFoundException(exception, id) },
        ) {
            httpClient.post("$BASE_PATH/$id/unpause")
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
    public suspend fun remove(id: String, options: ContainerRemoveOptions = ContainerRemoveOptions()) {
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
    }

    /**
     * Returns low-level information about a container.
     *
     * @param id ID or name of the container.
     * @param size Should return the size of container as fields `SizeRw` and `SizeRootFs`
     */
    public suspend fun inspect(id: String, size: Boolean = false): Container {
        return requestCatching(
            NotFound to { ContainerNotFoundException(it, id) },
        ) {
            httpClient.get("$BASE_PATH/$id/json") {
                parameter("size", size)
            }
        }.body()
    }

    public fun logs(id: String, callback: YokiFlow<Frame>): Closeable = InternalYokiFlow().also { flow ->
        flow.start(flow = logs(id), callback = callback)
    }

    public suspend fun prune(): ContainerPruneResult {
        return httpClient.post("$BASE_PATH/prune").body()
    }

    public suspend fun prune(filters: ContainerPruneFilters): ContainerPruneResult {
        return httpClient.post("$BASE_PATH/prune") {
            parameter("filters", json.encodeToString(filters))
        }.body()
    }

    public suspend fun wait(id: String, condition: String? = null): ContainerWaitResult {
        return httpClient.post("$BASE_PATH/$id/wait") {
            parameter("condition", condition)
        }.body()
    }

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

    /**
     * Resizes the TTY for a container.
     *
     * @param container Unique identifier or name of the container.
     * @param options Resize options like width and height.
     * @throws ContainerNotFoundException If the container is not found.
     * @throws YokiResponseException If the container cannot be resized or if an error occurs in the request.
     */
    public suspend fun resizeTTY(container: String, options: ResizeTTYOptions) {
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
    }

    /**
     * Runs a command inside a running container.
     *
     * @param container Unique identifier or name of the container.
     * @param options Exec instance command options.
     */
    public suspend fun exec(container: String, options: ExecCreateOptions): String {
        return requestCatching(
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
    }
}