<p align="center">
    <img alt="Yoki logo" src="https://github.com/DevNatan/yoki/assets/24600258/f4069f5f-f980-470f-9d36-d37b6d0943c8" width="300">
</p>


# Yoki

[![Build](https://github.com/DevNatan/yoki/actions/workflows/build.yml/badge.svg)](https://github.com/DevNatan/yoki/actions/workflows/build.yml)
[![Integration Tests](https://github.com/DevNatan/yoki/actions/workflows/integration-tests.yml/badge.svg)](https://github.com/DevNatan/yoki/actions/workflows/integration-tests.yml)
![Maven Central](https://img.shields.io/maven-central/v/me.devnatan/yoki)

Yoki allows you to interact with the Docker Engine Remote API in a simplified and fast way.

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("me.devnatan:yoki:0.3.0")
}
```

For Java users, use the Yoki JVM artifact

```groovy
implementation 'me.devnatan:yoki-jvm:0.3.0'
```

## Getting Started

Use `Yoki.create()` to create a new Yoki client instance with the default settings, default settings are based on the 
current platform or environment variables, e.g.: socket path will be set to [`DOCKER_HOST`](https://docs.docker.com/compose/environment-variables/envvars/#docker_host)
if present otherwise `unix://var/run/docker.sock` if the current platform is Unix-like.

```kotlin
val client = Yoki.create()
```

To change the default configuration properties use `YokiConfig` and `Yoki` overload.

```kotlin
val client = Yoki {
    // this: YokiConfigBuilder
}
```

In Java code you can use `YokiConfigBuilder` with `YokiConfig.builder()`.

```java
YokiConfig config = YokiConfig.builder().socketPath(...).build()
Yoki client = Yoki.create(config)
```

To Docker resources, functions will return `CompletableFuture<T>` or `YokiFlow<T>` (for streaming) due to Java Interoperatibility
but there are extensions for Kotlin that are `suspend` and for streaming returns `Flow<T>`.

##### Get System Information

```kotlin
val version: SystemVersion = client.system.version()
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
```java
final YokiFlow<Frame> callback = new YokiFlow<Frame>() {
    @Override
    public void onEach(Frame log) { /* do something with each log */ }

    @Override
    public void onStart() { /* streaming started */ }

    @Override
    public void onComplete(Throwable error) { /* streaming finished */ }
    
    @Override
    public void onError(Throwable cause) { /* something went wrong */ }
};

client.containers.logsAsync("floral-fury", callback);

// Short version
client.containers.logsAsync("floral-fury", (log) -> /* do something with each log */);
```

## License

Yoki is licensed under the MIT license.
