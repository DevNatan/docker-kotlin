package me.devnatan.yoki.resource.image

import me.devnatan.yoki.YokiResourceException

public open class ImageException internal constructor(
    cause: Throwable?,
) : YokiResourceException(cause)

public class ImageNotFoundException internal constructor(
    cause: Throwable?,
    public val image: String,
) : ImageException(cause)
