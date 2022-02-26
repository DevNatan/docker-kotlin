# Yoki
[![Build](https://github.com/KatanPanel/yoki/actions/workflows/build.yml/badge.svg)](https://github.com/KatanPanel/yoki/actions/workflows/build.yml)
[![Integration Tests](https://github.com/KatanPanel/yoki/actions/workflows/integration-tests.yml/badge.svg)](https://github.com/KatanPanel/yoki/actions/workflows/integration-tests.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.katan/yoki)](https://mvnrepository.com/artifact/org.katan)
[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.png?v=103)](https://github.com/ellerbrock/open-source-badges/)

**Yoki is a multiplatform Docker API client.**

This project allows you to interact with the container runtimes API like Docker Engine API in a simplified and fast way, Yoki was built under Kotlin Multiplatform so you can use it either in JVM projects with Kotlin or Kotlin Native. 

The initial purpose of creating the project was to have integration with other projects of the Katan organization, since existing projects that implemented clients for the Docker API always had some problems, the most common of which being lack of support, lack of documentation and poor performance. 

* [Project Setup](#project-setup)
* [Initializing the Client](#initializing-the-client)
* [Engines](#engines)
* [Contributing](#contributing)
* [Third Party](#third-party)
* [License](#license)

## Project Setup
This library is published to Maven Central.\
Remember to add the Maven Central repository if it isn't already there:
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.katan:yoki-core-jvm:0.0.1")
}
```

###### Multiplatform
In multiplatform projects, add a dependency to the commonMain source set dependencies.
```kotlin
commonMain {
    dependencies {
        implementation("org.katan:yoki-core:0.0.1")
    }
}
```

## Initializing the Client
The Yoki client is the central point of all its operation, it is through it that you will access the API of the resources that are currently supported by Yoki and its configuration as well. For the Yoki client to work, you need a container engine that you specify when creating the client instance.

We will explain more about engines later on.
```kotlin
public val yoki = Yoki(...)
```

Specify client configurations by expanding the function.
```kotlin
public val yoki = Yoki(...) {
    // ...
}
```

## Engines
Yoki's first functionality intention was for it to be a client that only interacted with the Docker API, but we realized that other engines were emerging over time and that eventually we would want to add support for them in the future, so we prepared the code to accept it. different types of engines besides Docker, that's why you have to specify an engine in the client as said before.

For now, only Docker is supported, feel free to contribute if you want to make your own engine implementation which is not yet supported.

### Docker
Before using, see if the endpoints you are targeting are supported in the Docker [Supported Endpoints](https://github.com/KatanPanel/yoki/blob/main/yoki-engine-docker/README.md) section.\
We expect to achieve 100% coverage over the entire Docker API for all versions starting with v1.41, soon.

To add Yoki's Docker engine to your project, add the respective artifact.

```groovy
dependencies {
    implementation("org.katan:yoki-engine-docker-jvm:0.0.1")
}
```

In multiplatform projects, add a dependency to the commonMain source set dependencies.
```kotlin
commonMain {
    dependencies {
        implementation("org.katan:yoki-engine-docker:0.0.1")
    }
}
```

Then configure in the client initialization step.
```kotlin
public val yoki = Yoki(Docker) {
    engine { 
        // DockerEngineConfig
    }
}
```

## Contributing
Yoki is open-source and we lovingly accept any contribution to the project. 

If you are thinking of contributing to the project, check out the [Contribution Guide](https://github.com/KatanPanel/yoki/blob/main/CONTRIBUTING.md) to ensure that everything looks good for everyone who will use it and that the project remains consistent and maintainable.

## Third Party
Third party software used in Katan is listed in [this file](THIRDPARTY.md).

## License
Yoki is licensed under the MIT license.
