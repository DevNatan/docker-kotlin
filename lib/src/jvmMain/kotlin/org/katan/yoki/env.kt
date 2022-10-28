package org.katan.yoki

public actual fun env(key: String): String? {
    return System.getenv(key)
}
