package org.katan.yoki.resource.swarm

import org.katan.yoki.YokiResourceException

public open class SwarmException internal constructor(
    message: String?,
    cause: Throwable?,
    properties: Map<String, Any?>,
) : YokiResourceException(message, cause, properties)

public class NodeNotPartOfSwarmException internal constructor(
    message: String?,
    cause: Throwable?,
    properties: Map<String, Any?>,
) : SwarmException(message, cause, properties)
