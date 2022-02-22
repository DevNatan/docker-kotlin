@file:JvmMultifileClass
package org.katan.yoki.protocol

import org.katan.yoki.protocol.JvmUnixSocket.Companion.decodeHostname
import java.io.*
import java.net.*
import java.nio.file.Files
import java.nio.file.Paths
import javax.net.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import okio.ByteString.Companion.decodeHex
import org.newsclub.net.unix.*

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

internal class JvmUnixSocket : Socket() {

    private lateinit var socket: AFUNIXSocket

    @Throws(IOException::class)
    override fun connect(endpoint: SocketAddress, timeout: Int) {
        val address = (endpoint as InetSocketAddress).address
        val socketAddress = getSocketAddress(decodeHostname(address))
        println("$endpoint ($socketAddress) - timeout: $timeout")

        socket = AFUNIXSocket.connectTo(socketAddress)
        socket.soTimeout = timeout
    }

    @Throws(IOException::class)
    override fun bind(bindpoint: SocketAddress?) {
        socket.bind(bindpoint)
    }

    override fun isConnected(): Boolean = socket.isConnected
    @Throws(IOException::class) override fun getOutputStream(): OutputStream = socket.outputStream
    @Throws(IOException::class) override fun getInputStream(): InputStream = socket.inputStream

    override fun close() {
        socket.close()
    }

    companion object {
        fun decodeHostname(address: InetAddress): String {
            return decodeHostname(address.hostName)
        }

        fun decodeHostname(hostName: String): String {
            return hostName.substring(0, hostName.indexOf(".socket")).decodeHex().utf8()
        }
    }
}

public class UnixSocketFactory : AFUNIXSocketFactory() {
    override fun addressFromHost(host: String, port: Int): AFUNIXSocketAddress {
        return getSocketAddress(decodeHostname(host))
    }
}
