package me.devnatan.yoki.api.external.resource.container

public interface ContainerResource {

    /**
     * Creates a new container.
     *
     * @return the newly created container id.
     * @see <a href="https://docs.podman.io/en/latest/_static/api.html#operation/ContainerCreateLibpod">containers/create</a>
     */
    public suspend fun create(options: Map<String, Any>): String

}