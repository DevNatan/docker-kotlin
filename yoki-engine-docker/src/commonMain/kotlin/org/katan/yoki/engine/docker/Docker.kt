package org.katan.yoki.engine.docker

import org.katan.yoki.*
import org.katan.yoki.engine.*
import org.katan.yoki.engine.docker.resource.container.*
import org.katan.yoki.engine.docker.resource.network.*
import org.katan.yoki.engine.docker.resource.volume.*

public object Docker : YokiEngineFactory<DockerEngineConfig> {

    override fun create(block: DockerEngineConfig.() -> Unit): YokiEngine {
        return DockerEngine(DockerEngineConfig().apply(block))
    }

}

public val Yoki.container: ContainerResource get() = (engine as DockerEngine).containerResource
public val Yoki.network: NetworkResource get() = (engine as DockerEngine).networkResource
public val Yoki.volume: VolumeResource get() = (engine as DockerEngine).volumeResource