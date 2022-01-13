package org.katan.yoki.engine.docker

import org.katan.yoki.api.*
import org.katan.yoki.engine.docker.resource.container.*
import org.katan.yoki.engine.docker.resource.network.*

public val Yoki.container: ContainerResource get() = (this as Docker).container
public val Yoki.network: NetworkResource get() = (this as Docker).network