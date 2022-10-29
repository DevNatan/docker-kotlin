package org.katan.yoki

public open class YokiException : Exception {
    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

public open class YokiResourceException internal constructor(
    message: String?,
    cause: Throwable?,
    public val properties: Map<String, Any?>,
) : YokiException(message, cause)

public open class UnhandledYokiResourceException internal constructor(
    message: String?,
    cause: Throwable?,
    properties: Map<String, Any?>,
) : YokiResourceException(message, cause, properties)
