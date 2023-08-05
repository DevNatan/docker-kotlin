package me.devnatan.yoki.resource.container

import me.devnatan.yoki.YokiResourceException

public open class ContainerException internal constructor(
    cause: Throwable?,
) : YokiResourceException(cause)

public class ContainerAlreadyStartedException internal constructor(
    cause: Throwable?,
    public val containerId: String,
) : ContainerException(cause)

public class ContainerAlreadyStoppedException internal constructor(
    cause: Throwable?,
    public val containerId: String,
) : ContainerException(cause)

public class ContainerAlreadyExistsException internal constructor(
    cause: Throwable?,
    public val name: String,
) : ContainerException(cause)

public class ContainerNotFoundException internal constructor(
    cause: Throwable?,
    public val containerId: String,
) : ContainerException(cause)

public class ContainerRemoveConflictException internal constructor(
    cause: Throwable?,
    public val containerId: String,
) : ContainerException(cause)

public class ContainerRenameConflictException internal constructor(
    cause: Throwable?,
    public val containerId: String,
    public val newName: String,
) : ContainerException(cause)

public class ContainerNotRunningException internal constructor(
    cause: Throwable?,
    public val containerId: String?,
) : ContainerException(cause)
