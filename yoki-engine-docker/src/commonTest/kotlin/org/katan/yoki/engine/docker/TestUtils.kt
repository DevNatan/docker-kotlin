package org.katan.yoki.engine.docker

import org.katan.yoki.*

/**
 * Creates a new Yoki instance for testing.
 * @param block The configuration factory.
 */
fun createTestYoki(block: YokiConfig<DockerEngineConfig>.() -> Unit = {}): Yoki {
    return Yoki(Docker) { apply(block) }
}
