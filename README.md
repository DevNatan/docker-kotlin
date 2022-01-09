# Yoki
[![Maven Central](https://img.shields.io/maven-central/v/org.katan/yoki)](https://mvnrepository.com/artifact/org.katan)
[![GitHub License](https://img.shields.io/github/license/KatanPanel/yoki)](https://github.com/KatanPanel/yoki/blob/main/LICENSE)

Yoki is a multiplatform container engine API client.

* [Using in your project](#using-in-your-project)
* [Supported Engines](#supported-engines)
* [License](#license)

## Using in your project
This library is published to Maven Central.

### Gradle
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

### Maven
```xml
<dependency>
    <groupId>org.katan</groupId>
    <artifactId>yoki-core-jvm</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Supported Engines
For now only Docker is supported but we plan to add support for other engines in the future.
Feel free to contribute if you want to make your own engine implementation that is not yet supported.

### Docker
To add Yoki's Docker engine to your project, add the respective artifact.

#### Maven
```xml
<dependency>
    <groupId>org.katan</groupId>
    <artifactId>yoki-engine-docker-jvm</artifactId>
    <version>0.0.1</version>
</dependency>
```

#### Gradle
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

## License
Yoki is licensed under the MIT license.