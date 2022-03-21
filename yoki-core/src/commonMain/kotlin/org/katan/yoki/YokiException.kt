package org.katan.yoki

public open class YokiException : Exception {
    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

public open class YokiResourceException(
    message: String?,
    cause: Throwable?,
    public val properties: Map<String, Any?>
) : YokiException(message, cause)

public open class ContainerException(
    message: String?,
    cause: Throwable?,
    properties: Map<String, Any?>
) : YokiResourceException(message, cause, properties) {

    public val containerId: String by properties

    public companion object {
        public const val CONTAINER_ID_PROPERTY: String = "containerId"
    }

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