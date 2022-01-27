package org.katan.yoki.resource.container

import kotlinx.serialization.Serializable

@Serializable
public data class ContainerRemoveOptions(
    public var removeAnonymousVolumes: Boolean = false,
    public var force: Boolean = false,
    public var unlink: Boolean = false
)
