# docker-kotlin

[![Build](https://github.com/DevNatan/docker-kotlin/actions/workflows/build.yml/badge.svg)](https://github.com/DevNatan/docker-kotlin/actions/workflows/build.yml)
[![Integration Tests](https://github.com/DevNatan/docker-kotlin/actions/workflows/integration-tests.yml/badge.svg)](https://github.com/DevNatan/docker-kotlin/actions/workflows/integration-tests.yml)
![Maven Central](https://img.shields.io/maven-central/v/me.devnatan/docker-kotlin)

docker-kotlin allows you to interact with the Docker Engine Remote API in a simplified and fast way.

* [Installation](#installation)
* [Basic Usage](#basic-usage)
* [Supported Endpoints](SUPPORTED_ENDPOINTS.md)

## Installation

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("me.devnatan:docker-kotlin:0.6.0")
}
```

For JVM users, use the Docker client JVM artifact

```groovy
implementation 'me.devnatan:docker-kotlin-jvm:0.6.0'
```

## Basic Usage

Use `DockerKotlin.create()` to create a new Docker client instance with the default settings, default settings are based on the 
current platform or environment variables, e.g.: socket path will be set to [`DOCKER_HOST`](https://docs.docker.com/compose/environment-variables/envvars/#docker_host)
if present otherwise `unix://var/run/docker.sock` if the current platform is Unix-like.

```kotlin
val client = DockerClient.create()
```

To change the default configuration properties use `DockerClientConfig` and `DockerClient` overload.

```kotlin
val client = DockerClient {
    // this: DockerClientConfigBuilder
}
```

##### Get System Information

```kotlin
val version: SystemVersion = client.system.version()
```

##### Create and start a Container with explicit port bindings

```kotlin
val containerId = client.containers.create("busybox:latest") {
    // Only if your container doesn't already expose this port
    exposedPort(80u)

    hostConfig {
        portBindings(80u) {
            add(PortBinding("0.0.0.0", 8080u))
        }
    }
}

client.containers.start(containerId)
```

##### Create and start a Container with auto-assigned port bindings

```kotlin
val containerId = client.containers.create("busybox:latest") {
    // Only if your container doesn't already expose this port
    exposedPort(80u)
    
    hostConfig {
        portBindings(80u)
    }
}

client.containers.start(containerId)

// Inspect the container to retrieve the auto-assigned ports
val container = testClient.containers.inspect(id)
val ports = container.networkSettings.ports
```

##### List All Containers

```kotlin
val containers: List<Container> = client.containers.list()
```

##### Create a new Network

```kotlin
val networkId: String = client.networks.create {
    name = "octopus-net"
    driver = "overlay"
}
```

##### Stream Container Logs

```kotlin
val logs: Flow<Frame> = client.containers.logs("floral-fury") {
    stderr = true
    stdout = true
}

logs.onStart { /* streaming started */ }
    .onCompletion { /* streaming finished */ }
    .catch { /* something went wrong */ }
    .collect { log -> /* do something with each log */ }
```

## License

docker-kotlin is licensed under the MIT license.
