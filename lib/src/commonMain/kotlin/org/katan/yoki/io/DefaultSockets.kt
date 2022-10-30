@file:JvmName("DefaultSockets")

package org.katan.yoki.io

import kotlin.jvm.JvmName

internal const val UNIX_SOCKET_PREFIX = "unix://"
internal const val HTTP_SOCKET_PREFIX = "tcp://"

public const val DEFAULT_DOCKER_UNIX_SOCKET: String = "${UNIX_SOCKET_PREFIX}/var/run/docker.sock"
public const val DEFAULT_DOCKER_HTTP_SOCKET: String = "${HTTP_SOCKET_PREFIX}localhost:2375"
