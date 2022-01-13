package org.katan.yoki.engine

import kotlinx.coroutines.CoroutineScope

public interface YokiEngine : CoroutineScope {

    public val config: YokiEngineConfig

}
