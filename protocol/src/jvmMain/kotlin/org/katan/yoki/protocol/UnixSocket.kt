package org.katan.yoki.protocol

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.newsclub.net.unix.AFUNIXSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Thread safe non-persistent socket connection, closes the connection after a request.
 */
public actual open class SingleUnixSocket actual constructor(socketPath: String) : UnixSocket {

    private val address: AFUNIXSocketAddress = getSocketAddress(socketPath)
    public actual val socketPath: String get() = address.path

    public actual override fun close() {
        // this socket connection is not persistent
    }

}

public actual class PersistentUnixSocket actual constructor(socketPath: String) : UnixSocket {

    private val socket: AFUNIXSocket = AFUNIXSocket.connectTo(getSocketAddress(socketPath))

    private val writer: Writer = OutputStreamWriter(socket.outputStream)
    private val reader: BufferedReader = socket.inputStream.bufferedReader()

    public actual override fun close() {
        socket.close()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    public actual suspend fun write(value: String) {
        writer.write(value)
    }

    public actual suspend fun read(): Flow<String> {
        return reader.lineSequence().asFlow()
    }

}

private fun getSocketAddress(path: String): AFUNIXSocketAddress {
    val socketPath = Paths.get(path) ?: error("unable to connect to unix socket @ $path")

    if (!Files.exists(socketPath) || !Files.isWritable(socketPath))
        error("socket path do not exists or is not writable: $socketPath")

    return AFUNIXSocketAddress.of(socketPath)
}