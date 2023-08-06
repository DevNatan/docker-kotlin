@file:JvmSynthetic

package me.devnatan.yoki.resource.container

import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.prepareGet
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.ByteOrder
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.devnatan.yoki.models.Frame
import me.devnatan.yoki.models.ResizeTTYOptions
import me.devnatan.yoki.models.Stream
import me.devnatan.yoki.models.container.ContainerCreateOptions
import me.devnatan.yoki.models.container.ContainerListOptions
import me.devnatan.yoki.models.container.ContainerLogsOptions
import me.devnatan.yoki.models.container.ContainerPruneFilters
import me.devnatan.yoki.models.container.ContainerPruneResult
import me.devnatan.yoki.models.container.ContainerRemoveOptions
import me.devnatan.yoki.models.container.ContainerSummary
import me.devnatan.yoki.models.exec.ExecCreateOptions

public suspend inline fun ContainerResource.list(block: ContainerListOptions.() -> Unit): List<ContainerSummary> {
    return list(ContainerListOptions().apply(block))
}

public suspend inline fun ContainerResource.create(block: ContainerCreateOptions.() -> Unit): String {
    return create(ContainerCreateOptions().apply(block))
}

public suspend inline fun ContainerResource.remove(id: String, block: ContainerRemoveOptions.() -> Unit) {
    return remove(id, ContainerRemoveOptions().apply(block))
}

public inline fun ContainerResource.logs(
    id: String,
    block: ContainerLogsOptions.() -> Unit,
): Flow<Frame> {
    return logs(id, ContainerLogsOptions().apply(block))
}

public suspend inline fun ContainerResource.prune(block: ContainerPruneFilters.() -> Unit): ContainerPruneResult {
    return prune(ContainerPruneFilters().apply(block))
}

/**
 * Resizes the TTY for a container.
 *
 * @param container Unique identifier or name of the container.
 * @param block Resize options like width and height.
 * @throws ContainerNotFoundException If the container is not found.
 */
public suspend inline fun ContainerResource.resizeTTY(
    container: String,
    block: ResizeTTYOptions.() -> Unit,
) {
    resizeTTY(container, ResizeTTYOptions().apply(block))
}

/**
 * Runs a command inside a running container.
 *
 * @param container Unique identifier or name of the container.
 * @param block Exec instance command options.
 */
public suspend inline fun ContainerResource.exec(
    container: String,
    block: ExecCreateOptions.() -> Unit,
) {
    exec(container, ExecCreateOptions().apply(block))
}

public fun ContainerResource.logs(id: String): Flow<Frame> = logs(
    id,
    options = ContainerLogsOptions(
        follow = true,
        stderr = true,
        stdout = true,
    ),
)

public fun ContainerResource.logs(id: String, options: ContainerLogsOptions): Flow<Frame> = flow {
    httpClient.prepareGet("${ContainerResource.BASE_PATH}/$id/logs") {
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

            // Unknown stream = tty enabled
            if (stream == null) {
                val remaining = channel.availableForRead

                // Remaining +1 includes the previously read first byte. Reinsert the first byte since we read it
                // before but the type was not expected, so this byte is actually the first character of the line.
                val len = remaining + 1
                val payload = ByteReadChannel(
                    ByteArray(len) {
                        if (it == 0) fb else channel.readByte()
                    },
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
