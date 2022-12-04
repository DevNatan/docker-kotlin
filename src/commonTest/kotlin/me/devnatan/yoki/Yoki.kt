package me.devnatan.yoki

/**
 * Creates a new Yoki instance for testing.
 * @param block The client configuration factory.
 */
fun createTestYoki(block: YokiConfigBuilder.() -> Unit = {}): Yoki {
    return runCatching {
        Yoki { apply(block) }
    }.onFailure {
        @Suppress("TooGenericExceptionThrown")
        throw RuntimeException("Failed to initialize Yoki test client", it)
    }.getOrThrow()
}
