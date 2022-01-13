package org.katan.yoki.engine.docker

import org.katan.yoki.api.*

public class DockerConfigBuilder : YokiConfigBuilder() {

    override var apiVersion: String = "1.41"
    private var socketPath: String = "unix://docker.sock"

    override fun build(): YokiConfig = DockerConfig(apiVersion, socketPath)

}

public class DockerConfig internal constructor(
    apiVersion: String,
    public val socketPath: String,
) : BaseYokiConfig(apiVersion)