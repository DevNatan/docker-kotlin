package org.katan.yoki

import kotlinx.coroutines.*
import org.katan.yoki.engine.*
import kotlin.coroutines.*

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

/**
 * DslMarker for Yoki.
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
public annotation class YokiDsl
