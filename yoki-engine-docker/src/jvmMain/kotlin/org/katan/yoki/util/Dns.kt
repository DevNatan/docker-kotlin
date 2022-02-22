package org.katan.yoki.util

import okhttp3.*
import java.net.*

internal class SocketDns : Dns {

    override fun lookup(hostname: String): List<InetAddress> {
        return if (hostname.endsWith(".socket")) listOf(
            InetAddress.getByAddress(
                hostname,
                byteArrayOf(0, 0, 0, 0)
            )
        ) else Dns.SYSTEM.lookup(hostname)
    }
}
