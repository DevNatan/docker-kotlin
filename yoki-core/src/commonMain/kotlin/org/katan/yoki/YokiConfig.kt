package org.katan.yoki

public class YokiConfig<T : YokiEngineConfig> {

    internal var engineConfig: T.() -> Unit = {}
}

public fun <T : YokiEngineConfig> YokiConfig<T>.engine(block: T.() -> Unit) {
    engineConfig = block
}
