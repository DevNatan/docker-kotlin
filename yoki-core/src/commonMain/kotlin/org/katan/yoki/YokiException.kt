package org.katan.yoki

public open class YokiException : Exception {
    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

public class ContainerAlreadyStartedException(
    public val containerId: String
) : YokiException()

public class ContainerNotFoundException(
    public val containerId: String
) : YokiException()
