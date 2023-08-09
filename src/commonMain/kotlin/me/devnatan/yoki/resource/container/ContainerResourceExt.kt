package me.devnatan.yoki.resource.container

import me.devnatan.yoki.YokiResponseException
import me.devnatan.yoki.models.ResizeTTYOptions
import me.devnatan.yoki.models.container.ContainerCreateOptions
import me.devnatan.yoki.models.container.ContainerListOptions
import me.devnatan.yoki.models.container.ContainerPruneFilters
import me.devnatan.yoki.models.container.ContainerPruneResult
import me.devnatan.yoki.models.container.ContainerRemoveOptions
import me.devnatan.yoki.models.container.ContainerSummary
import me.devnatan.yoki.models.exec.ExecCreateOptions
import me.devnatan.yoki.resource.image.ImageNotFoundException

/**
 * Returns a list of all containers.
 *
 * @param options Options to customize the listing result.
 */
public suspend inline fun ContainerResource.list(options: ContainerListOptions.() -> Unit): List<ContainerSummary> {
    return list(ContainerListOptions().apply(options))
}

/**
 * Creates a new container.
 *
 * @param options Options to customize the container creation.
 * @throws ImageNotFoundException If the image specified does not exist or isn't pulled.
 * @throws ContainerAlreadyExistsException If a container with the same name already exists.
 */
public suspend inline fun ContainerResource.create(options: ContainerCreateOptions.() -> Unit): String {
    return create(ContainerCreateOptions().apply(options))
}

/**
 * Removes a container.
 *
 * @param container The container id to remove.
 * @param options Removal options.
 * @throws ContainerNotFoundException If the container is not found for the specified id.
 * @throws ContainerRemoveConflictException When trying to remove an active container without the `force` option.
 */
public suspend inline fun ContainerResource.remove(container: String, options: ContainerRemoveOptions.() -> Unit) {
    return remove(container, ContainerRemoveOptions().apply(options))
}

public suspend inline fun ContainerResource.prune(block: ContainerPruneFilters.() -> Unit): ContainerPruneResult {
    return prune(ContainerPruneFilters().apply(block))
}

/**
 * Resizes the TTY for a container.
 *
 * @param container The container id to resize.
 * @param options Resize options like width and height.
 * @throws ContainerNotFoundException If the container is not found.
 * @throws YokiResponseException If the container cannot be resized or if an error occurs in the request.
 */
public suspend inline fun ContainerResource.resizeTTY(container: String, options: ResizeTTYOptions.() -> Unit) {
    resizeTTY(container, ResizeTTYOptions().apply(options))
}

/**
 * Runs a command inside a running container.
 *
 * @param container The container id to execute the command.
 * @param options Exec instance command options.
 */
public suspend inline fun ContainerResource.exec(container: String, options: ExecCreateOptions.() -> Unit) {
    exec(container, ExecCreateOptions().apply(options))
}

// public inline fun ContainerResource.logs(
//     id: String,
//     block: ContainerLogsOptions.() -> Unit,
// ): Flow<Frame> {
//     return logs(id, ContainerLogsOptions().apply(block))
// }

// public fun ContainerResource.logs(id: String): Flow<Frame> = logs(
//     id,
//     options = ContainerLogsOptions(
//         follow = true,
//         stderr = true,
//         stdout = true,
//     ),
// )

// public fun ContainerResource.logs(id: String, options: ContainerLogsOptions): Flow<Frame> = flow {
//     httpClient.prepareGet("${ContainerResource.BASE_PATH}/$id/logs") {
//         parameter("follow", options.follow)
//         parameter("stdout", options.stdout)
//         parameter("stderr", options.stderr)
//         parameter("since", options.since)
//         parameter("until", options.until)
//         parameter("timestamps", options.showTimestamps)
//         parameter("tail", options.tail)
//     }.execute { response ->
//         val channel = response.body<ByteReadChannel>()
//         while (!channel.isClosedForRead) {
//             val fb = channel.readByte()
//             val stream = Stream.typeOfOrNull(fb)
//
//             // Unknown stream = tty enabled
//             if (stream == null) {
//                 val remaining = channel.availableForRead
//
//                 // Remaining +1 includes the previously read first byte. Reinsert the first byte since we read it
//                 // before but the type was not expected, so this byte is actually the first character of the line.
//                 val len = remaining + 1
//                 val payload = ByteReadChannel(
//                     ByteArray(len) {
//                         if (it == 0) fb else channel.readByte()
//                     },
//                 )
//
//                 val line = payload.readUTF8Line() ?: error("Payload cannot be null")
//
//                 // Try to determine the "correct" stream since we cannot have this information.
//                 val stdoutEnabled = options.stdout ?: false
//                 val stdErrEnabled = options.stderr ?: false
//                 val expectedStream: Stream = stream ?: when {
//                     stdoutEnabled && !stdErrEnabled -> Stream.StdOut
//                     stdErrEnabled && !stdoutEnabled -> Stream.StdErr
//                     else -> Stream.Unknown
//                 }
//
//                 emit(Frame(line, len, expectedStream))
//                 continue
//             }
//
//             val header = channel.readPacket(7)
//
//             // We discard the first three bytes because the payload size is in the last four bytes
//             // and the total header size is 8.
//             header.discard(3)
//
//             val payloadLength = header.readInt(ByteOrder.BIG_ENDIAN)
//             val payloadData = channel.readUTF8Line(payloadLength)!!
//             emit(Frame(payloadData, payloadLength, stream))
//         }
//     }
// }
