# Yoki

[![Build](https://github.com/KatanPanel/yoki/actions/workflows/build.yml/badge.svg)](https://github.com/KatanPanel/yoki/actions/workflows/build.yml)
[![Integration Tests](https://github.com/KatanPanel/yoki/actions/workflows/integration-tests.yml/badge.svg)](https://github.com/KatanPanel/yoki/actions/workflows/integration-tests.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.katan/yoki)](https://mvnrepository.com/artifact/org.katan)
[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.png?v=103)](https://github.com/ellerbrock/open-source-badges/)
</center>

Yoki allows you to interact with the container runtimes API like Docker Engine API in a simplified and fast way, Yoki was built under Kotlin Multiplatform so you can use it either in Kotlin or Kotlin Native projects.

The initial purpose of creating the project was to have integration with other projects of the Katan organization, since existing projects that implemented clients for the Docker API always had some problems, the most common of which being bad of support, lack of documentation and specially poor performance.

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.katan:yoki:0.0.1") // KMP
    implementation("org.katan:yoki-jvm:0.0.1") // JVM
}
```

## Usage
The Yoki client, it is through it that you will access the API of the resources that are currently supported by Yoki.

```kotlin
val yoki = Yoki()
```

Specify client configurations by expanding the function.
```kotlin
val yoki = Yoki {
    // this: YokiConfig
}
```

## Code samples
#### Listing containers

```kotlin
yoki.containers.list() // List<Container>
```

Up to 5 containers
```kotlin
yoki.containers.list {
    limit = 5
}
```

From a specific network
```kotlin
yoki.containers.list {
    filters {
        network = "octopus-network"
    }
}
```

#### Creating a container
```kotlin
// will return the newly created container id
yoki.containers.create {
    name = "billie-jean"
}
```

#### Fetching containers logs
Streaming methods will always return a [Flow](https://kotlinlang.org/docs/flow.html).

You need to specify where the logs can come from (stdout/stderr)
```kotlin
yoki.containers.logs("floral-fury") {
    stderr = true
    stdout = true
}
```

Fetching only STDERR
```kotlin
yoki.containers.logs("ruze-of-an-ooze") {
    stderr = true
}
```

From an instant. `long` and [kotlinx-datetime.Instant](https://github.com/Kotlin/kotlinx-datetime) types are supported. 
```kotlin
yoki.containers.logs("botanic-panic") {
    stdout = true
    since = 1666999925L // long
    since = "2022-10-28T22:19:44.475Z".toInstant() // kotlinx-datetime.Instant
}
```

## License
Yoki is licensed under the MIT license.
