package org.katan.yoki.engine.docker.model.container

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.katan.yoki.engine.docker.model.HostConfig
import org.katan.yoki.engine.docker.model.Port

/**
 * @author João Victor Gomides Cruz
 */
@Serializable
public data class Container(
    @SerialName("Id") val id: String,
    @SerialName("Names") val names: List<String>,
    @SerialName("Image") val image: String,
    @SerialName("ImageID") val imageId: String,
    @SerialName("Command") val command: String,
    @SerialName("Created") val created: String?,
    @SerialName("State") val state: String?,
    @SerialName("Status") val status: String?,
    @SerialName("Ports") val ports: List<Port>?,
    @SerialName("Labels") val labels: Map<String, String>?,
    @SerialName("SizeRw") val sizeRw: Long,
    @SerialName("SizeRootFs") val sizeRootFs: Long,
    @SerialName("HostConfig") val hostConfig: HostConfig?,
    @SerialName("NetworkSettings") val networkSettings: NetworkSettings?,
    @SerialName("Mounts") val mounts: List<Mount>?,
)

/**
 * The name the container will be given. If no name is provided, one will be randomly generated.
 */
public const val ContainerName: String = "name"

/**
 * The namespace the container will be placed in.
 */
public const val ContainerNamespace: String = "namespace"

/**
 * The image the container will be based on.
 *
 * The image will be used as the container's root filesystem, and its environment vars, volumes,
 * and other configuration will be applied to the container. Conflicts with Rootfs.
 *
 * At least one of Image or Rootfs must be specified.
 */
public const val ContainerImage: String = "image"
