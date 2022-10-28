@file:JvmName("DefaultSockets")

package org.katan.yoki.io

import kotlin.jvm.JvmName

public const val DEFAULT_DOCKER_UNIX_SOCKET: String = "unix:///var/run/docker.sock"
public const val DEFAULT_DOCKER_TCP_SOCKET: String = "tcp://localhost:2375"
