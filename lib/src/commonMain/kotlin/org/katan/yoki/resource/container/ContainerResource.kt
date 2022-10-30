package org.katan.yoki.resource.container

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.prepareGet
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.ByteOrder
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.katan.yoki.io.requestCatching
import org.katan.yoki.resource.Frame
import org.katan.yoki.resource.IdOnlyResponse
import org.katan.yoki.resource.Stream
import kotlin.time.Duration

public class ContainerResource internal constructor(
    private val httpClient: HttpClient,
    private val json: Json,
) {

    private companion object {
        private const val BASE_PATH = "/containers"
        private val LINE_BREAK_REGEX: Regex = Regex("\\r\\n|\\n|\\r")
    }

    /**
     * Returns a list of all created containers.
     */
    public suspend fun list(): List<ContainerBasicInfo> {
        return list(
            ContainerListOptions(
                all = true
            )
        )
    }

    /**
     * Returns a list of all containers.
     *
     * @param options Options to customize the listing result.
     */
    public suspend fun list(options: ContainerListOptions): List<ContainerBasicInfo> {
        return httpClient.get("$BASE_PATH/json") {
            parameter("all", options.all)
            parameter("limit", options.limit)
            parameter("size", options.size)
            parameter("filters", json.encodeToString(options.filters))
        }.body()
    }

    /**
     * Creates a new container.
     */
    public suspend fun create(options: ContainerCreateOptions): String {
        requireNotNull(options.image) { "Container Image is required" }

        // TODO print warnings
        return httpClient.post("$BASE_PATH/create") {
            parameter("name", options.name)
            setBody(options)
        }.body<IdOnlyResponse>().id
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
            HttpStatusCode.NotModified to { ContainerAlreadyStartedException(it, id) },
            HttpStatusCode.NotFound to { ContainerNotFoundException(it, id) },
        ) {
            httpClient.post("$BASE_PATH/$id/start") {
                parameter("detachKeys", detachKeys)
            }
        }
    }

    public suspend fun stop(id: String, timeout: Int? = null) {
        httpClient.post("$BASE_PATH/$id/stop") {
            parameter("t", timeout)
        }
    }

    public suspend fun restart(id: String, timeout: Int? = null) {
        httpClient.post("$BASE_PATH/$id/restart") {
            parameter("t", timeout)
        }
    }

    public suspend fun kill(id: String, signal: String? = null) {
        httpClient.post("$BASE_PATH/$id/kill") {
            parameter("signal", signal)
        }
    }

    public suspend fun rename(id: String, newName: String) {
        httpClient.post("$BASE_PATH/$id/rename") {
            parameter("name", newName)
        }
    }

    public suspend fun pause(id: String) {
        httpClient.post("$BASE_PATH/$id/pause")
    }

    public suspend fun unpause(id: String) {
        httpClient.post("$BASE_PATH/$id/unpause")
    }

    public suspend fun remove(id: String) {
        requestCatching(
            HttpStatusCode.NotFound to { ContainerNotFoundException(it, id) },
            HttpStatusCode.Conflict to { ContainerRemoveConflictException(it, id) }
        ) {
            httpClient.delete("$BASE_PATH/$id")
        }
    }

    public suspend fun remove(id: String, options: ContainerRemoveOptions) {
        httpClient.delete("$BASE_PATH/$id") {
            parameter("v", options.removeAnonymousVolumes)
            parameter("force", options.force)
            parameter("link", options.unlink)
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
            HttpStatusCode.NotFound to { ContainerNotFoundException(it, id) }
        ) {
            httpClient.get("$BASE_PATH/$id/json") {
                parameter("size", size)
            }
        }.body()
    }

    public fun logs(id: String): Flow<Frame> {
        return logs(id) {
            follow = true
            stderr = true
            stdout = true
        }
    }

    public fun logs(id: String, options: ContainerLogsOptions): Flow<Frame> = flow {
        httpClient.prepareGet("$BASE_PATH/$id/logs") {
            parameter("follow", options.follow)
            parameter("stdout", options.stdout)
            parameter("stderr", options.stderr)
            parameter("since", options.since)
            parameter("until", options.until)
            parameter("timestamps", options.showTimestamps)
            parameter("tail", options.tail)
        }.execute { response ->
            val channel = response.body<ByteReadChannel>()
            while (!channel.isClosedForRead) {
                val fb = channel.readByte()
                val stream = Stream.typeOfOrNull(fb)
                // println("[debug] TTY: ${stream == null}")

                // Unknown stream = tty enabled
                if (stream == null) {
                    val remaining = channel.availableForRead

                    // Remaining +1 includes the previously read first byte. Reinsert the first byte since we read it
                    // before but the type was not expected, so this byte is actually the first character of the line.
                    val len = remaining + 1
                    val payload = ByteReadChannel(
                        ByteArray(len) {
                            if (it == 0) fb else channel.readByte()
                        }
                    )

                    val line = payload.readUTF8Line() ?: error("Payload cannot be null")

                    // Try to determine the "correct" stream since we cannot have this information.
                    val stdoutEnabled = options.stdout ?: false
                    val stdErrEnabled = options.stderr ?: false
                    val expectedStream: Stream = stream ?: when {
                        stdoutEnabled && !stdErrEnabled -> Stream.StdOut
                        stdErrEnabled && !stdoutEnabled -> Stream.StdErr
                        else -> Stream.Unknown
                    }

                    emit(Frame(line, len, expectedStream))
                    continue
                }

                val header = channel.readPacket(7)

                // We discard the first three bytes because the payload size is in the last four bytes
                // and the total header size is 8.
                header.discard(3)

                val payloadLength = header.readInt(ByteOrder.BIG_ENDIAN)
                val payloadData = channel.readUTF8Line(payloadLength)!!
                emit(Frame(payloadData, payloadLength, stream))
            }
        }
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
                val line = channel.readUTF8Line()
                if (line == null) {
                    println("Null")
                    break
                }

                println("Remaining: ${channel.availableForRead}")

                // TODO handle stream type
                emit(Frame(line, line.length, Stream.StdOut))
            }

            println("End")
        }
    }
}

public suspend inline fun ContainerResource.restart(id: String, timeout: Duration) {
    return restart(id, timeout.inWholeSeconds.toInt())
}

public suspend inline fun ContainerResource.stop(id: String, timeout: Duration) {
    return stop(id, timeout.inWholeSeconds.toInt())
}

public suspend inline fun ContainerResource.list(block: ContainerListOptions.() -> Unit): List<ContainerBasicInfo> {
    return list(ContainerListOptions().apply(block))
}

public suspend inline fun ContainerResource.create(block: ContainerCreateOptions.() -> Unit): String {
    return create(ContainerCreateOptions().apply(block))
}

public suspend inline fun ContainerResource.remove(id: String, block: ContainerRemoveOptions.() -> Unit) {
    return remove(id, ContainerRemoveOptions().apply(block))
}

public inline fun ContainerResource.logs(id: String, block: ContainerLogsOptions.() -> Unit): Flow<Frame> {
    return logs(id, ContainerLogsOptions().apply(block))
}

public suspend inline fun ContainerResource.prune(block: ContainerPruneFilters.() -> Unit): ContainerPruneResult {
    return prune(ContainerPruneFilters().apply(block))
}
