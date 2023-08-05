import io.gitlab.arturbosch.detekt.Detekt

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
version = "0.1.0"

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
    }

    val isNativeTargetsEnabled: Boolean = project.property("yoki.native-targets")
        ?.toString()
        ?.toBoolean()
        ?: true

    if (isNativeTargetsEnabled) {
        val hostOs = System.getProperty("os.name")
        val isMingwX64 = hostOs.startsWith("Windows")
        when {
            hostOs == "Mac OS X" -> macosX64("native")
            hostOs == "Linux" -> linuxX64("native")
            isMingwX64 -> mingwX64("native")
            else -> throw GradleException("Host OS is not supported in Kotlin Native: $hostOs")
        }
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

        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
                runtimeOnly(libs.junixsocket.native)
                implementation(libs.junixsocket.common)
                implementation(libs.ktor.client.engine.okhttp)
                implementation(libs.slf4j.api)
            }
        }

        val jvmTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }

        if (isNativeTargetsEnabled) {
            val nativeMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(libs.ktor.client.engine.cio)
                }
            }

            val nativeTest by getting {
                dependsOn(commonTest)
            }
        }
    }
}

val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")
publishOnCentral {
    projectDescription.set("Multiplatform Docker API client")
    projectLongName.set("yoki")
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

    withType<Detekt>().configureEach {
        jvmTarget = "11"

        reports {
            xml.required.set(true)
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("$projectDir/config/detekt.yml"))
    baseline = file("$projectDir/config/baseline.xml")
}