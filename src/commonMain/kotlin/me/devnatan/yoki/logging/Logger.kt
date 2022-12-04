package me.devnatan.yoki.logging

public expect class Logger {

    public fun dbg(message: String)

    public fun info(message: String)

    public fun warn(message: String)

    public fun error(message: String, cause: Throwable?)
}

public fun Logger.error(exception: Throwable) {
    error(exception.message ?: "Exception ${exception::class}", exception)
}

internal expect fun createLogger(): Logger
