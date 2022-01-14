// https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    application
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    jvm {
        withJava()
        compilations {
            val main = getByName("main")
            tasks {
                register<Jar>("jvmFatJar") {
                    group = "build"
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

                    dependsOn(build)
                    manifest {
                        attributes["Main-Class"] = "org.katan.yoki.engine.docker.MainKt"
                    }
                    from(configurations.getByName("runtimeClasspath").map { if (it.isDirectory) it else zipTree(it) }, main.output.classesDirs)
                    archiveBaseName.set("${project.name}-fat")
                }
            }
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
            filter {
                isFailOnNoMatchingTests = false
            }
            testLogging {
                showExceptions = true
                showStandardStreams = true
                events = setOf(
                    org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
                )
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            }
        }
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(project(":yoki-engine"))
                implementation(project(":yoki-protocol"))
                implementation(libs.bundles.ktor)
                implementation(libs.bundles.ktx)
                implementation(libs.ktx.datetime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.ktx.coroutines.test)
                implementation(libs.ktor.client.mock)
            }
        }

        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(project(":yoki-protocol"))
            }
        }

        val jvmTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }
    }
}