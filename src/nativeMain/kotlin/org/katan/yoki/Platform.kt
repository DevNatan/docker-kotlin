package org.katan.yoki

import kotlinx.cinterop.toKString
import platform.posix.getenv

/**
 * Returns if the current platform is a UNIX-based platform.
 */
internal actual fun isUnixPlatform(): Boolean {
    TODO()
}

/**
 * Gets the value of the specified environment variable.
 * An environment variable is a system-dependent external named value
 */
internal actual fun env(key: String): String? {
    return getenv(key)?.toKString()
}
