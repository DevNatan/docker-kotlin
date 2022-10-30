package org.katan.yoki.resource.exec

import org.katan.yoki.YokiResourceException

public open class ExecException(
    cause: Throwable?,
) : YokiResourceException(cause)

public class ExecNotFoundException(
    cause: Throwable?,
    public val execId: String,
) : YokiResourceException(cause)
