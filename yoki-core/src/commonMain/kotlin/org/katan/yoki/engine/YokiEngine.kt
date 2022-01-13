package org.katan.yoki.engine

import kotlinx.coroutines.*

public interface YokiEngine : CoroutineScope {

    public val config: YokiEngineConfig

}