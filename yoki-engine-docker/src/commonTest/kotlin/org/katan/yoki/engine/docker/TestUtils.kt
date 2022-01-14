package org.katan.yoki.engine.docker

import org.katan.yoki.*

fun createYoki(block: YokiConfig<DockerEngineConfig>.() -> Unit = {}): Yoki {
    return Yoki(Docker) { apply(block) }
}