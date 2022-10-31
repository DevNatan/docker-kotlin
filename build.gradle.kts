// https://youtrack.jetbrains.com/issue/KTIJ-19369
@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.publishOnCentral)
}

group = "org.katan"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    jvm {
        tasks.named<Test>("jvmTest") {
            useJUnitPlatform()
            testLogging {
                showExceptions = true
                showStandardStreams = true
                events = setOf(org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED, org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED)
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            }
        }
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    when {
        hostOs == "Mac OS X" -> macosX64("native")
        isMingwX64 || hostOs == "Linux" -> linuxX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin Native: $hostOs")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.ktx.coroutines.core)
                implementation(libs.ktx.datetime)
                implementation(libs.bundles.ktor)
                implementation(libs.bundles.ktx)
                implementation(libs.okio)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.ktx.coroutines.test)
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
                runtimeOnly(libs.junixsocket.native)
                implementation(libs.junixsocket.common)
                implementation(libs.ktor.client.engine.okhttp)
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val nativeMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.client.engine.cio)
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val nativeTest by getting {
            dependsOn(commonTest)
        }
    }
}

val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")
publishOnCentral {
    projectDescription.set("Multiplatform Docker API client")
    projectLongName.set(if (project.name == "lib") "yoki" else project.name)
    licenseName.set("MIT")
    licenseUrl.set("https://github.com/DevNatan/yoki/blob/main/LICENSE")
    projectUrl.set("https://github.com/DevNatan/yoki")
    scmConnection.set("git:git@github.com:DevNatan/yoki")

    mavenCentral.user.set(System.getenv("OSSRH_USERNAME"))
    mavenCentral.password.set(provider { System.getenv("OSSRH_PASSWORD") })

    repository("https://maven.pkg.github.com/DevNatan/yoki", "GitHub") {
        user.set(System.getenv("GITHUB_USERNAME"))
        password.set(System.getenv("GITHUB_TOKEN"))
    }

    if (!isReleaseVersion)
        mavenCentralSnapshotsRepository()
}

signing {
    isRequired = isReleaseVersion && gradle.taskGraph.hasTask("uploadArchives")
    useInMemoryPgpKeys(
        System.getenv("OSSRH_SIGNING_KEY"),
        System.getenv("OSSRH_SIGNING_PASSWORD")
    )
}

publishing {
    publications {
        withType<MavenPublication> {
            if ("OSSRH" !in name) {
                artifact(tasks.javadocJar)
            }

            pom {
                developers {
                    developer {
                        name.set("Natan Vieira")
                        email.set("natanvnascimento@gmail.com")
                        url.set("http://www.devnatan.me/")
                    }
                }
            }
        }
    }
}

tasks {
    check {
        dependsOn("installKotlinterPrePushHook")
    }
}
