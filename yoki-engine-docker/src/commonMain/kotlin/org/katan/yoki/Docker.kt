package org.katan.yoki

import org.katan.yoki.engine.YokiEngine
import org.katan.yoki.engine.YokiEngineFactory
import org.katan.yoki.resource.container.ContainerResource
import org.katan.yoki.resource.network.NetworkResource
import org.katan.yoki.resource.secret.SecretResource
import org.katan.yoki.resource.volume.VolumeResource

public object Docker : YokiEngineFactory<DockerEngineConfig> {

    override fun create(block: DockerEngineConfig.() -> Unit): YokiEngine {
        return DockerEngine(DockerEngineConfig().apply(block))
    }
}

public val Yoki.containers: ContainerResource get() = (engine as DockerEngine).containerResource
public val Yoki.networks: NetworkResource get() = (engine as DockerEngine).networkResource
public val Yoki.volumes: VolumeResource get() = (engine as DockerEngine).volumeResource
public val Yoki.secrets: SecretResource get() = (engine as DockerEngine).secretResource
