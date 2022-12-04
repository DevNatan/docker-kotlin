package me.devnatan.yoki.resource

import me.devnatan.yoki.YokiResourceException

public open class ExecException internal constructor(
    cause: Throwable?,
) : YokiResourceException(cause)

public class ExecNotFoundException internal constructor(
    cause: Throwable?,
    public val execId: String,
) : ExecException(cause)
