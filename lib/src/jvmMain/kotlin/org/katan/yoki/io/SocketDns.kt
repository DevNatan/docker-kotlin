package org.katan.yoki.io

import okhttp3.Dns
import java.net.InetAddress

internal class SocketDns(
  val isUnixSocket: Boolean,
) : Dns {

    override fun lookup(hostname: String): List<InetAddress> {
        return if (isUnixSocket) listOf(
            InetAddress.getByAddress(
                hostname,
                byteArrayOf(0, 0, 0, 0)
            )
        ) else Dns.SYSTEM.lookup(hostname)
    }
}
