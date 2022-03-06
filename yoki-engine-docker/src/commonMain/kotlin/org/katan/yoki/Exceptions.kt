package org.katan.yoki

public class DockerNoSuchImageException(
    public val image: String
) : YokiException()
