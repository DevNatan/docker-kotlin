package org.katan.yoki.engine.docker

import org.katan.yoki.engine.*

public class DockerEngineConfig : YokiEngineConfig() {

    public var apiVersion: String = "1.41"
    public var socketPath: String = "unix://docker.sock"

}