package org.katan.yoki

import kotlinx.coroutines.*
import kotlin.coroutines.*
import org.katan.yoki.engine.*

public class Yoki(
    public val engine: YokiEngine,
    public val config: YokiConfig<*>
) : CoroutineScope {

    private val job = Job(engine.coroutineContext[Job])
    override val coroutineContext: CoroutineContext = engine.coroutineContext + job

}

@YokiDsl
public fun <T : YokiEngineConfig> Yoki(
    factory: YokiEngineFactory<T>,
    block: YokiConfig<T>.() -> Unit = {}
): Yoki {
    val config = YokiConfig<T>().apply(block)
    val engine = factory.create(config.engineConfig)

    return Yoki(engine, config)
}

@DslMarker
public annotation class YokiDsl