import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.publishOnCentral)
    alias(libs.plugins.binaryCompatibilityValidator)
    alias(libs.plugins.kover)
    alias(libs.plugins.detekt)
}

group = "me.devnatan"
version = "0.7.0-SNAPSHOT"

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
                events = setOf(
                    org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                )
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            }
        }
        tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                freeCompilerArgs += listOf("-Xjvm-default=all")
            }
        }
    }

    linuxX64()
    macosX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.ktx.coroutines.core)
                implementation(libs.ktx.datetime)
                implementation(libs.bundles.ktor)
                implementation(libs.bundles.ktx)
                implementation(libs.kotlinx.io.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.ktx.coroutines.test)
            }
        }

        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
                runtimeOnly(libs.junixsocket.native)
                implementation(libs.junixsocket.common)
                implementation(libs.ktor.client.engine.okhttp)
                implementation(libs.slf4j.api)
                api(libs.apache.compress)
            }
        }

        val jvmTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }

        val nativeMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.client.engine.cio)
            }
        }

        val nativeTest by creating {
            dependsOn(commonTest)
        }

        val linuxX64Main by getting { dependsOn(nativeMain) }
        val linuxX64Test by getting { dependsOn(nativeTest) }
        val macosX64Main by getting { dependsOn(nativeMain) }
        val macosX64Test by getting { dependsOn(nativeTest) }
    }
}

val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")
publishOnCentral {
    projectDescription.set("Multiplatform Docker API client")
    projectLongName.set(project.name)
    licenseName.set("MIT")
    licenseUrl.set("https://github.com/DevNatan/yoki/blob/main/LICENSE")
    projectUrl.set("https://github.com/DevNatan/yoki")
    scmConnection.set("git:git@github.com:DevNatan/yoki")

    mavenCentral.user.set(System.getenv("OSSRH_USERNAME"))
    mavenCentral.password.set(provider { System.getenv("OSSRH_PASSWORD") })

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

    withType<Detekt>().configureEach {
        jvmTarget = "11"

        reports {
            xml.required.set(true)
        }
    }

    // https://youtrack.jetbrains.com/issue/KT-46466/Kotlin-MPP-publishing-Gradle-7-disables-optimizations-because-of-task-dependencies
    val signingTasks = withType<Sign>()
    withType<AbstractPublishToMaven>().configureEach {
        dependsOn(signingTasks)
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("$projectDir/config/detekt.yml"))
    baseline = file("$projectDir/config/baseline.xml")
}