package me.devnatan.yoki.impl.docker

import me.devnatan.yoki.api.*

internal val DefaultDockerConfig = DockerConfig("", "v1.4.0")

public class DockerConfigBuilder : ResourceConfigBuilder<DockerConfig> {

    public var socketPath: String = ""
    public var apiVersion: String = ""

    override fun build(): DockerConfig = DockerConfig(socketPath, apiVersion)
}

public class DockerConfig internal constructor(
    apiVersion: String,
    public val socketPath: String,
) : BaseYokiConfig(apiVersion)