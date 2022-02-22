package org.katan.yoki

import org.katan.yoki.engine.YokiEngineConfig

public class YokiConfig<T : YokiEngineConfig> {

    internal var engineConfig: T.() -> Unit = {}
}

public fun <T : YokiEngineConfig> YokiConfig<T>.engine(block: T.() -> Unit) {
    engineConfig = block
}
