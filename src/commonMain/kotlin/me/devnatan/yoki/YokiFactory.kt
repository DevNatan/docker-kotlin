@file:JvmSynthetic

package me.devnatan.yoki

import me.devnatan.yoki.YokiConfigBuilder.Companion.DEFAULT_DOCKER_API_VERSION
import kotlin.jvm.JvmSynthetic

/**
 * Creates a new Yoki instance with platform default socket path and [DEFAULT_DOCKER_API_VERSION] Docker API version
 * that'll be merged with specified configuration.
 *
 * @param configure The client configuration.
 */
public inline fun Yoki(crossinline configure: YokiConfigBuilder.() -> Unit): Yoki =
    Yoki(
        YokiConfigBuilder()
            .forCurrentPlatform()
            .apply(configure)
            .build(),
    )
