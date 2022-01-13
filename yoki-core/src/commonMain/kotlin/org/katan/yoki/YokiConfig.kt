package org.katan.yoki

import org.katan.yoki.engine.*

public class YokiConfig<T : YokiEngineConfig> {

    internal var engineConfig: T.() -> Unit = {}

}