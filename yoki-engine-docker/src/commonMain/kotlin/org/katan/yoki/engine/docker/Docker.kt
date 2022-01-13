package org.katan.yoki.engine.docker

import org.katan.yoki.api.*
import org.katan.yoki.engine.docker.resource.container.*
import org.katan.yoki.engine.docker.resource.network.*

public class Docker(
    override val config: DockerConfig
) : Yoki {

    override val engine: DockerEngine = DockerEngine(createHttpClient(this))
    internal val container: ContainerResource = ContainerResource(engine)
    internal val network: NetworkResource = NetworkResource(engine)

}

public inline fun Yoki(config: DockerConfigBuilder.() -> Unit = {}): Yoki {
    return Docker(DockerConfigBuilder().apply(config).build() as DockerConfig)
}