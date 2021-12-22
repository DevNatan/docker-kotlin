package org.katan.engine.container.properties

/**
 * The name the container will be given. If no name is provided, one will be randomly generated.
 */
public const val ContainerName: String = "name"

/**
 * The namespace the container will be placed in.
 */
public const val ContainerNamespace: String = "namespace"

/**
 * ID of the pod the container will join.
 */
public const val ContainerPod: String = "pod"

/**
 * The image the container will be based on.
 *
 * The image will be used as the container's root filesystem, and its environment vars, volumes,
 * and other configuration will be applied to the container. Conflicts with Rootfs.
 *
 * At least one of Image or Rootfs must be specified.
 */
public const val ContainerImage: String = "image"