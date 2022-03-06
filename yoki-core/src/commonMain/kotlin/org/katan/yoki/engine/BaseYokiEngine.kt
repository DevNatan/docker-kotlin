package org.katan.yoki.engine

import kotlin.coroutines.CoroutineContext

public open class BaseYokiEngine(
    public override val config: YokiEngineConfig
) : YokiEngine {

    override val coroutineContext: CoroutineContext
        get() = error("Not implemented")

}