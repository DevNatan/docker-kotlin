package org.katan.yoki.resource.container

import org.katan.yoki.YokiResourceException

public open class ContainerException(
    message: String?,
    cause: Throwable?,
    properties: Map<String, Any?>
) : YokiResourceException(message, cause, properties) {

    public val containerId: String by properties
}

public class ContainerAlreadyStartedException(
    message: String?,
    cause: Throwable?,
    properties: Map<String, Any?>
) : ContainerException(message, cause, properties)

public class ContainerNotFoundException(
    message: String?,
    cause: Throwable?,
    properties: Map<String, Any?>
) : ContainerException(message, cause, properties)

public class ContainerRemoveConflictException(
    message: String?,
    cause: Throwable?,
    properties: Map<String, Any?>
) : ContainerException(message, cause, properties)
