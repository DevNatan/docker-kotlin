@file:JvmName("PlatformJvm")

package me.devnatan.yoki.util

internal actual fun env(key: String): String? {
    return System.getenv(key)
}

internal actual fun isUnixPlatform(): Boolean {
    val os = System.getProperty("os.name").lowercase()
    return arrayOf("nix", "nux", "anix", "mac").any { os.contains(it) }
}

public actual typealias Closeable = java.io.Closeable
