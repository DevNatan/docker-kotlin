package org.katan.yoki.logging

private const val LOGGER_NAME = "Yoki"

private typealias LogLevel = Pair<Int, String>

private val DEBUG: LogLevel = 0 to "DEBUG"
private val INFO: LogLevel = 1 to "INFO"
private val WARN: LogLevel = 2 to "WARN"
@Suppress("MagicNumber")
private val ERROR: LogLevel = 3 to "ERROR"

public actual class Logger {

    private val level: LogLevel = INFO

    private fun log(level: LogLevel, message: String, cause: Throwable? = null) {
        if (level.first < this.level.first) return
        println(
            buildString {
                append("[${level.second}] ")
                append("($LOGGER_NAME): ")
                append(message)
                if (cause != null)
                    append(". Cause: ${cause.stackTraceToString()}")
            }
        )
    }

    public actual fun dbg(message: String) {
        log(DEBUG, message)
    }

    public actual fun info(message: String) {
        log(INFO, message)
    }

    public actual fun warn(message: String) {
        log(WARN, message)
    }

    public actual fun error(message: String, cause: Throwable?) {
        log(ERROR, message, cause)
    }
}

internal actual fun createLogger(): Logger {
    return Logger()
}
