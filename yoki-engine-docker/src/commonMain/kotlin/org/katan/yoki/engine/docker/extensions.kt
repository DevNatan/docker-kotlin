package org.katan.yoki.engine.docker

import org.katan.yoki.api.*
import org.katan.yoki.engine.docker.resource.container.*

public val Yoki.container: ContainerResource get() = (this as Docker).container