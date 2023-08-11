package me.devnatan.yoki.io

import okhttp3.Dns
import okio.ByteString.Companion.decodeHex
import org.newsclub.net.unix.AFUNIXSocketAddress
import org.newsclub.net.unix.AFUNIXSocketFactory
import java.net.InetAddress
import java.nio.file.Files
import java.nio.file.Paths

internal class SocketDns(private val isUnixSocket: Boolean) : Dns {

    override fun lookup(hostname: String): List<InetAddress> {
        return if (isUnixSocket) {
            listOf(
                InetAddress.getByAddress(
                    hostname,
                    byteArrayOf(0, 0, 0, 0),
                ),
            )
        } else {
            Dns.SYSTEM.lookup(hostname)
        }
    }
}

internal class UnixSocketFactory : AFUNIXSocketFactory() {
    private fun decodeHostname(hostname: String): String {
        return hostname
            .substring(0, hostname.indexOf(ENCODED_HOSTNAME_SUFFIX))
            .decodeHex()
            .utf8()
    }

    override fun addressFromHost(host: String, port: Int): AFUNIXSocketAddress {
        val socketPath = decodeHostname(host)
        val socketFile = Paths.get(socketPath) ?: error("Unable to connect to unix socket @ $socketPath")

        if (!Files.exists(socketFile) || !Files.isWritable(socketFile)) {
            error("Socket path do not exists or is not writable: $socketFile")
        }

        return AFUNIXSocketAddress.of(socketFile)
    }
}
