package me.devnatan.yoki.resource.secret

import me.devnatan.yoki.YokiResourceException

public open class SecretException internal constructor(
    cause: Throwable?,
) : YokiResourceException(cause)

public class SecretNotFoundException internal constructor(
    cause: Throwable?,
    public val secretId: String,
    public val version: Long,
) : SecretException(cause)

public class SecretNameConflictException internal constructor(
    cause: Throwable?,
    public val secretName: String?,
) : SecretException(cause)
