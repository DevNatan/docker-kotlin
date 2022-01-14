@file:JvmMultifileClass
package org.katan.yoki.protocol

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import okio.ByteString.Companion.decodeHex
import okio.ByteString.Companion.encodeUtf8
import org.newsclub.net.unix.AFUNIXSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import java.io.*
import java.net.*
import java.nio.file.Files
import java.nio.file.Paths
import javax.net.*

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
        println("endpoint: $endpoint")

        val address = (endpoint as InetSocketAddress).address
        println("address: $address")

        val socketPath = decodeHostname(address)
        println("connect via: $socketPath")

        val socketFile = File(socketPath)
        println("socket file: $socketFile")
        
        socket = AFUNIXSocket.newInstance()
        socket.connect(AFUNIXSocketAddress.of(socketFile), timeout)
        socket.soTimeout = timeout
    }

    @Throws(IOException::class)
    override fun bind(bindpoint: SocketAddress?) {
        socket.bind(bindpoint)
    }

    override fun isConnected(): Boolean = socket.isConnected
    @Throws(IOException::class) override fun getOutputStream(): OutputStream = socket.outputStream
    @Throws(IOException::class) override fun getInputStream(): InputStream = socket.inputStream

    private object Encoder {
        fun encode(text: String): String {
            return text.encodeUtf8().hex()
        }

        fun decode(hex: String): String {
            return hex.decodeHex().utf8()
        }
    }

    companion object {
        fun encodeHostname(path: String): String {
            return Encoder.encode(path) + ".socket"
        }

        fun decodeHostname(address: InetAddress): String {
            val hostName = address.hostName
            return Encoder.decode(hostName.substring(0, hostName.indexOf(".socket")))
        }
    }

}

public class UnixSocketFactory : SocketFactory() {

    init {
        if (!AFUNIXSocket.isSupported()) {
            throw UnsupportedOperationException("AFUNIXSocket.isSupported() == false")
        }
    }

    @Throws(IOException::class)
    override fun createSocket(): Socket {
        return JvmUnixSocket()
    }

    @Throws(IOException::class)
    override fun createSocket(s: String, i: Int): Socket {
        throw UnsupportedOperationException()
    }

    @Throws(IOException::class)
    override fun createSocket(s: String, i: Int, inetAddress: InetAddress, i1: Int): Socket {
        throw UnsupportedOperationException()
    }

    @Throws(IOException::class)
    override fun createSocket(inetAddress: InetAddress, i: Int): Socket {
        throw UnsupportedOperationException()
    }

    @Throws(IOException::class)
    override fun createSocket(inetAddress: InetAddress, i: Int, inetAddress1: InetAddress, i1: Int): Socket {
        throw UnsupportedOperationException()
    }

}