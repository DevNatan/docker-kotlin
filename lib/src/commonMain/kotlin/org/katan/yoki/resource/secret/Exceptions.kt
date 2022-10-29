package org.katan.yoki.resource.secret

import org.katan.yoki.YokiResourceException

public open class SecretException internal constructor(
    message: String?,
    cause: Throwable?,
    properties: Map<String, Any?>
) : YokiResourceException(message, cause, properties)

public class SecretNotFoundException internal constructor(
    message: String?,
    cause: Throwable?,
    properties: Map<String, Any?>
) : SecretException(message, cause, properties) {

    public val id: String by properties
    public val version: Long by properties
}

public class SecretNameConflictException internal constructor(
    message: String?,
    cause: Throwable?,
    properties: Map<String, Any?>
) : SecretException(message, cause, properties) {

    public val secretName: String by properties
}
