# Yoki for Docker
Docker Client API implementation.

```kotlin
dependencies {
  implementation("org.katan:yoki-engine-docker:0.0.1")
}
```

Create a client with default configuration.
```kotlin
val client = Yoki(Docker)
```

You can specify any [DockerEngineConfig](https://github.com/KatanPanel/yoki/blob/main/yoki-engine-docker/src/commonMain/kotlin/org/katan/yoki/engine/docker/DockerConfig.kt#L10) option in the client factory builder.
```kotlin
val client = Yoki(Docker) {
    engine {
        apiVersion = "1.40"
    }
}
```

You will find examples of how to use the commands in the documentation for the respective resources.
First, check if that [endpoint is supported](https://github.com/KatanPanel/yoki/blob/main/yoki-engine-docker/SUPPORTED_ENDPOINTS.md).