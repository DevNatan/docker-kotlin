package me.devnatan.yoki.resource

import me.devnatan.yoki.YokiResourceException

public open class ImageException internal constructor(
    cause: Throwable?,
) : YokiResourceException(cause)

public class ImageNotFoundException internal constructor(
    cause: Throwable?,
    public val image: String,
) : ImageException(cause)
