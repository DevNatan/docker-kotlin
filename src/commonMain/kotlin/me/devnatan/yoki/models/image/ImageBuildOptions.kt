package me.devnatan.yoki.models.image

import me.devnatan.yoki.models.RegistryConfig

/**
 * Options for building a Docker image.
 *
 * @property path The path to the build context.
 * @property tag The tag to be applied to the built image.
 * @property extraHosts Additional hosts to add to /etc/hosts in the build containers.
 * @property remote URL of the Docker daemon to connect to.
 * @property suppressVerbose Suppress verbose output during build.
 * @property noCache Do not use the cache when building the image.
 * @property cacheFrom List of images used for cache resolution.
 * @property pull Always attempt to pull a newer version of the image.
 * @property removeIntermediateContainers Remove intermediate containers after a successful build.
 * @property forceRemoveIntermediateContainers Force removal of intermediate containers.
 * @property memoryLimit Memory limit for the build container.
 * @property memorySwap Swap limit equal to memory plus swap.
 * @property cpuShares CPU shares for the build container.
 * @property cpuSetCpus CPUs in which to allow execution.
 * @property cpuPeriod CPU period to be used for the build container.
 * @property cpuQuota CPU quota to be used for the build container.
 * @property buildArgs Build-time variables to set during the build.
 * @property shmSize Size of /dev/shm in bytes.
 * @property squash Squash newly built layers into a single new layer.
 * @property labels User-defined key-value metadata to add to the image.
 * @property networkMode Network mode for the build container.
 * @property platform Platform in the format os[/arch[/variant]].
 * @property target Build only a specific target stage.
 * @property outputs Specify the outputs to be built.
 * @property version Version of the build options.
 * @property registryConfig Docker registry auth configurations for multiple registries that a build may refer to.
 */
public data class ImageBuildOptions(
    public val path: String? = null,
    public val tag: String? = null,
    public val extraHosts: String? = null,
    public val remote: String? = null,
    public val suppressVerbose: Boolean? = null,
    public val noCache: Boolean? = null,
    public val cacheFrom: List<String> = emptyList(),
    public val pull: String? = null,
    public val removeIntermediateContainers: Boolean? = null,
    public val forceRemoveIntermediateContainers: Boolean? = null,
    public val memoryLimit: Int? = null,
    public val memorySwap: Int? = null,
    public val cpuShares: Int? = null,
    public val cpuSetCpus: String? = null,
    public val cpuPeriod: Int? = null,
    public val cpuQuota: Int? = null,
    public val buildArgs: Map<String, String> = emptyMap(),
    public val shmSize: Int? = null,
    public val squash: Boolean? = null,
    public val labels: Map<String, String> = emptyMap(),
    public val networkMode: String? = null,
    public val platform: String? = null,
    public val target: String? = null,
    public val outputs: String? = null,
    public val version: String? = null,
    public val registryConfig: Map<String, RegistryConfig> = emptyMap(),
)
