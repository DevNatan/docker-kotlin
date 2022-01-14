# Yoki for Docker

* [Project Setup](#project-setup)
* [Configuration](#configuration)
* [Resources](#resources)
  * [Network](#network)
    * [Listing all networks](#listing-all-networks)
    * [Create network](#create-network)
    * [Prune networks](#prune-networks)

## Project Setup
```groovy
dependencies {
    implementation("org.katan:yoki-engine-docker:0.0.1")
}
```

## Configuration
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

## Resources
First, check if that feature is already supported in the [supported endpoint list](https://github.com/KatanPanel/yoki/blob/main/yoki-engine-docker/SUPPORTED_ENDPOINTS.md).

### Network
#### Listing all networks
```kotlin
val networks: List<Network> = client.networks.list()
```

You can specify [NetworkFilters](https://github.com/KatanPanel/yoki/blob/main/yoki-engine-docker/src/commonMain/kotlin/org/katan/yoki/engine/docker/resource/network/NetworkResource.kt#L179) to filter API results.
```kotlin
val networks: List<Network> = client.networks.list {
    // filters network by specific scope
    scope = NetworkGlobalScope
}
```

#### Prune networks
Prune all networks
```kotlin
client.networks.prune()
```

Prune networks before a timestamp (use Docker duration string)
```kotlin
client.networks.prune {
    until = "30m"
}
```

You can prune all networks with a specific label too
```kotlin
client.networks.prune {
    label = "my-label"
}
```