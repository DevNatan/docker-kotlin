package org.katan.yoki.engine.docker

import org.katan.yoki.api.*


public class DockerConfigBuilder : YokiConfigBuilder<DockerConfig>() {

    override var apiVersion: String = "1.41"
    private var socketPath: String = "unix://docker.sock"

    override fun build(): DockerConfig = DockerConfig(this, socketPath)

}

public class DockerConfig internal constructor(
    baseConfig: YokiConfig,
    public val socketPath: String,
) : YokiConfig by baseConfig