package org.katan.yoki.resource

import org.katan.yoki.YokiResourceException

public open class SwarmException internal constructor(
    cause: Throwable?,
) : YokiResourceException(cause)

public class NodeNotPartOfSwarmException internal constructor(
    cause: Throwable?,
) : SwarmException(cause)
