package org.katan.yoki.api

public interface Yoki {

    public val config: YokiConfig

    public val engine: YokiEngine

}

public interface YokiEngine : Closeable {



}

public interface YokiConfig {

    public val apiVersion: String

}

public abstract class YokiConfigBuilder<C : YokiConfig> : YokiConfig {

    public override var apiVersion: String = ""
    public var engine: C? = null

    public abstract fun build(): C

}

public open class BaseYokiConfig(
    final override val apiVersion: String,
) : YokiConfig