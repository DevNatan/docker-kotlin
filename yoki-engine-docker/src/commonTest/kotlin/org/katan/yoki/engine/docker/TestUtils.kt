package org.katan.yoki.engine.docker

import org.katan.yoki.Docker
import org.katan.yoki.DockerEngineConfig
import org.katan.yoki.Yoki
import org.katan.yoki.YokiConfig

/**
 * Creates a new Yoki instance for testing.
 * @param block The configuration factory.
 */
fun createTestYoki(block: YokiConfig<DockerEngineConfig>.() -> Unit = {}): Yoki {
    return Yoki(Docker) { apply(block) }
}
