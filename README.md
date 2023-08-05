# Yoki

[![Build](https://github.com/DevNatan/yoki/actions/workflows/build.yml/badge.svg)](https://github.com/DevNatan/yoki/actions/workflows/build.yml)
[![Integration Tests](https://github.com/DevNatan/yoki/actions/workflows/integration-tests.yml/badge.svg)](https://github.com/DevNatan/yoki/actions/workflows/integration-tests.yml)

Yoki allows you to interact with the Docker Engine Remote API in a simplified and fast way.

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("me.devnatan:yoki-jvm:0.1.0")
}
```

## Configuration

By default, at client startup if no configuration parameters are passed, the settings that will be applied will depend
on the current platform and environment variables.

For example, the socket path will be set to the value of
the [`DOCKER_HOST` environment variable](https://docs.docker.com/compose/reference/envvars/#docker_host) if set,
otherwise it will use the platform default.

```kotlin
import me.devnatan.yoki

val dockerClient = Yoki()
```

You can still configure the client by expanding the initialization block

```kotlin
Yoki {
    // this: YokiConfigBuilder
}
```

Change socket path (docker host) or target api version

```kotlin
Yoki {
    socketPath = "unix:///var/run/docker.sock"
    apiVersion = "1.40"
}
```

## Usage

The way to access resources is straight to the point, all functions (for Kotlin) are suspend.

##### Get info about system version

```kotlin
val version: SystemVersion = dockerClient.system.version()
```

##### Listing all containers

```kotlin
val containers: List<Container> = dockerClient.containers.list {
    all = true
}
```

##### Creating a new network

```kotlin
val networkId: String = dockerClient.networks.create {
    name = "octopus-net"
    driver = "overlay"
}
```

##### Streaming container logs

All streaming methods will always return a [Flow](https://kotlinlang.org/docs/flow.html).

```kotlin
val logs: Flow<Frame> = dockerClient.containers.logs("floral-fury") {
    stderr = true
    stdout = true
}
```

#### Fallback to version-specific parameter value

By default, all options parameters for accessing a resource use `null`, that is, *null value* means
that it will use the value defined by the Docker API as the default value for that property.

## License

Yoki is licensed under the MIT license.