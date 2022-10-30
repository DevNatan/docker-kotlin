# Yoki

[![Build](https://github.com/KatanPanel/yoki/actions/workflows/build.yml/badge.svg)](https://github.com/KatanPanel/yoki/actions/workflows/build.yml)
[![Integration Tests](https://github.com/KatanPanel/yoki/actions/workflows/integration-tests.yml/badge.svg)](https://github.com/KatanPanel/yoki/actions/workflows/integration-tests.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.katan/yoki)](https://mvnrepository.com/artifact/org.katan)
[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.png?v=103)](https://github.com/ellerbrock/open-source-badges/)

Yoki allows you to interact with the Docker Remote API.

## Using in your projects

Only snapshots repository available for now
```kotlin
repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation("org.katan:yoki:0.0.1-SNAPSHOT")
}
```

## Usage
The Yoki client, it is through it that you will access the API of the resources that are currently supported by Yoki.
```kotlin
Yoki {
    // this: YokiConfig
}
```

Specifying a socket path
```kotlin
Yoki {
    socketPath = "..."
    version = "1.40"
}
```

Configure Yoki to use UNIX defaults
```kotlin
Yoki { 
    useUnixDefaults()
}

// equivalent to
Yoki {
    socketPath = "unix:///var/run/docker.sock"
}
```

Configure Yoki to use HTTP defaults
```kotlin
Yoki { useHTTPDefaults() }

// equivalent to
Yoki {
    socketPath = "tcp://localhost:2375"
}
```

## Calling from regular Java code
Yoki was not made exclusively for Kotlin, although we have support for Coroutines, there are plans for regular Java code 
to be able to call them and so the API will start to be designed with that in mind.

We haven't done anything yet, but in the future we will support calls using only Java.

## Examples
### Listing containers

```kotlin
yoki.containers.list()
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

### Creating a container
```kotlin
// will return the newly created container id
yoki.containers.create {
    name = "billie-jean"
}
```

### Fetching containers logs
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
    setSince("2022-10-28T22:19:44.475Z".toInstant()) // kotlinx-datetime.Instant
}
```

## License
Yoki is licensed under the MIT license.
