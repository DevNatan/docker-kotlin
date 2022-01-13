package org.katan.yoki.engine.docker

import org.katan.yoki.engine.*

/**
 * Docker engine configuration
 *
 * @see DockerEngine
 */
public class DockerEngineConfig : YokiEngineConfig() {

    /**
     * The version of the Docker API that will be used during communication.
     *
     * @see <a href="https://docs.docker.com/engine/api/#versioned-api-and-sdk">Versioned API and SDK</a>
     */
    public var apiVersion: String = "1.41"

    public var socketPath: String = "unix:///var/run/docker.sock"

}