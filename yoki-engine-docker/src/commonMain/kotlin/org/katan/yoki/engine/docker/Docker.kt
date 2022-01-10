package org.katan.yoki.engine.docker

import org.katan.yoki.api.*
import org.katan.yoki.engine.docker.resource.container.*

public class Docker(
    override val config: DockerConfig,
    override val engine: DockerEngine
) : Yoki {

    internal val container: ContainerResource = ContainerResource(engine)

}

public inline fun Yoki(config: DockerConfigBuilder.() -> Unit): Yoki {
    return Docker(DockerConfigBuilder().apply(config).build(), DockerEngine())
}