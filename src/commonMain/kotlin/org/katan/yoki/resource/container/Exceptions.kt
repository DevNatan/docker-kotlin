package org.katan.yoki.resource.container

import org.katan.yoki.YokiResourceException

public open class ContainerException(
    cause: Throwable?,
) : YokiResourceException(cause)

public class ContainerAlreadyStartedException(
    cause: Throwable?,
    public val containerId: String,
) : ContainerException(cause)

public class ContainerNotFoundException(
    cause: Throwable?,
    public val containerId: String,
) : ContainerException(cause)

public class ContainerRemoveConflictException(
    cause: Throwable?,
    public val containerId: String,
) : ContainerException(cause)

public class ContainerNotRunningException(
    cause: Throwable?,
    public val containerId: String?,
) : ContainerException(cause)
