package me.devnatan.yoki.resource.swarm

import me.devnatan.yoki.YokiResourceException

public open class SwarmException internal constructor(
    cause: Throwable?,
) : YokiResourceException(cause)

public class NodeNotPartOfSwarmException internal constructor(
    cause: Throwable?,
) : SwarmException(cause)
