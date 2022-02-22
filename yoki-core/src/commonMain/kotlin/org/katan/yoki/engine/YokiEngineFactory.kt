package org.katan.yoki.engine

public interface YokiEngineFactory<out T : YokiEngineConfig> {

    public fun create(block: T.() -> Unit = {}): YokiEngine
}
