# Yoki
[![Maven Central](https://img.shields.io/maven-central/v/org.katan/yoki)](https://mvnrepository.com/artifact/org.katan)
[![GitHub License](https://img.shields.io/github/license/KatanPanel/yoki)](https://github.com/KatanPanel/yoki/blob/main/LICENSE)

Yoki is a multiplatform container engine API client.

* [Using in your project](#using-in-your-project)
  * [Project Setup](#project-setup)
  * [Initializing the Client](#initializing-the-client)
  * [Supported Engines](#supported-engines)
* [Java integration](#java-integration)
* [Contributing](#contributing)
* [License](#license)

## Using in your project
### Project Setup
This library is published to Maven Central.

#### Gradle
Remember to add the Maven Central repository if it isn't already there:
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.katan:yoki-core-jvm:0.0.1")
}
```

In multiplatform projects, add a dependency to the commonMain source set dependencies.
```kotlin
commonMain {
    dependencies {
        implementation("org.katan:yoki-core:0.0.1")
    }
}
```

#### Maven
```xml
<dependency>
    <groupId>org.katan</groupId>
    <artifactId>yoki-core-jvm</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Initializing the Client
You can add your preferred engine to your project's classpath and use Yoki's agnostic startup function which will automatically identify which engine to use.
```kotlin
public val yoki = Yoki(Docker)
```

Specify client configurations by expanding the function.
```kotlin
public val yoki = Yoki(Docker) {
    // ...
}
```

### Supported Engines
For now only Docker is supported but we plan to add support for other engines in the future.
Feel free to contribute if you want to make your own engine implementation that is not yet supported.

You can configure a engine in the client initialization step.
```kotlin
public val yoki = Yoki(Docker) {
    engine { 
        // DockerEngineConfig
    }
}
```

#### Docker
Before using, see if the endpoints you are targeting are supported in the Docker [Supported Endpoints](https://github.com/KatanPanel/yoki/blob/main/yoki-engine-docker/README.md) section.
To add Yoki's Docker engine to your project, add the respective artifact.

##### Maven
```xml
<dependency>
    <groupId>org.katan</groupId>
    <artifactId>yoki-engine-docker-jvm</artifactId>
    <version>0.0.1</version>
</dependency>
```

##### Gradle
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

## Java integration
You can safely use Yoki in your Java project, we work to maintain decent compatibility with Java projects as the project is expected to be used in non-Kotlin projects too.
If you want to use Yoki in your Java project, use the artifact suffixed with `-jvm`.

## Contributing
Like all other Katan projects, Yoki is open-source and we lovingly accept any contribution to the project. 

If you are thinking of contributing to the project, check out the [Contribution Guide](https://github.com/KatanPanel/yoki/blob/main/CONTRIBUTING.md) to ensure that everything looks good for everyone who will use it and that the project remains consistent and maintainable.

## License
Yoki is licensed under the MIT license.
