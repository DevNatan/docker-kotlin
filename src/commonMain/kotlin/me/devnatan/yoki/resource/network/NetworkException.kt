package me.devnatan.yoki.resource

import me.devnatan.yoki.YokiResourceException

public open class NetworkException internal constructor(
    cause: Throwable?,
) : YokiResourceException(cause)

public class NetworkNotFoundException internal constructor(
    cause: Throwable?,
    public val networkId: String,
) : NetworkException(cause)
