package org.katan.yoki.io

import okio.ByteString.Companion.decodeHex
import org.newsclub.net.unix.AFUNIXSocketAddress
import org.newsclub.net.unix.AFUNIXSocketFactory
import java.nio.file.Files
import java.nio.file.Paths

private fun decodeHostname(hostName: String): String {
    return hostName
        .substring(0, hostName.indexOf(".socket"))
        .decodeHex()
        .utf8()
}

private fun getSocketAddress(path: String): AFUNIXSocketAddress {
    val socketPath = Paths.get(path) ?: error("unable to connect to unix socket @ $path")

    if (!Files.exists(socketPath) || !Files.isWritable(socketPath))
        error("socket path do not exists or is not writable: $socketPath")

    return AFUNIXSocketAddress.of(socketPath)
}

internal class UnixSocketFactory : AFUNIXSocketFactory() {
    override fun addressFromHost(host: String, port: Int): AFUNIXSocketAddress {
        return getSocketAddress(decodeHostname(host))
    }
}
