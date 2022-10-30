package org.katan.yoki.resource.container

import org.katan.yoki.YokiResourceException

public open class ContainerException(
    cause: Throwable?,
    public val containerId: String,
) : YokiResourceException(cause)

public class ContainerAlreadyStartedException(
    cause: Throwable?,
    containerId: String,
) : ContainerException(cause, containerId)

public class ContainerNotFoundException(
    cause: Throwable?,
    containerId: String,
) : ContainerException(cause, containerId)

public class ContainerRemoveConflictException(
    cause: Throwable?,
    containerId: String,
) : ContainerException(cause, containerId)
