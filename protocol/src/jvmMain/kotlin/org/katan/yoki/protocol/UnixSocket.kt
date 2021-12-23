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
public actual open class SingleUnixSocket(path: String) : UnixSocket {

    private val address: AFUNIXSocketAddress = getSocketAddress(path)
    public actual val socketPath: String get() = address.path

    actual override fun close() {
        // this socket connection is not persistent
    }

}

public actual class PersistentUnixSocket(path: String) : UnixSocket {

    private val socket: AFUNIXSocket = AFUNIXSocket.connectTo(getSocketAddress(path))

    private val writer: Writer = OutputStreamWriter(socket.outputStream)
    private val reader: BufferedReader = socket.inputStream.bufferedReader()

    actual override fun close() {
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