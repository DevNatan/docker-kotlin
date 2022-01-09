package me.devnatan.yoki.api

public interface Yoki<E, C : YokiConfigBuilder> : Closeable {

    public val config: C

    public fun invoke(config: C.() -> Unit)

    public override fun close()

}

public interface YokiConfig {

    public val apiVersion: String

}

public open class YokiConfigBuilder {

    public var apiVersion: String = ""

}

public open class BaseYokiConfig(
    final override val apiVersion: String,
) : YokiConfig