package org.katan.engine.container

public interface ContainerFactory {

    public suspend fun create(container: Container): String

}