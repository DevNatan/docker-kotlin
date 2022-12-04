// ktlint-disable filename

package org.katan.yoki.logging

import org.katan.yoki.Yoki
import org.slf4j.LoggerFactory
import org.slf4j.Logger as SLF4JLogger

public actual class Logger {

    public companion object {
        @Suppress("MemberNameEqualsClassName")
        private val logger: SLF4JLogger = LoggerFactory.getLogger(Yoki::class.java)
    }

    public actual fun dbg(message: String) {
        logger.debug(message)
    }

    public actual fun info(message: String) {
        logger.info(message)
    }

    public actual fun warn(message: String) {
        logger.warn(message)
    }

    public actual fun error(message: String, cause: Throwable?) {
        logger.error(message, cause)
    }
}

internal actual fun createLogger(): Logger {
    return Logger()
}
