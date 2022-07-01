// https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    id("org.jmailen.kotlinter") version "3.11.1" // TODO version catalog
    id("org.danilopianini.publish-on-central") version "2.0.2" // TODO version catalog
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.danilopianini.publish-on-central")
    apply(plugin = "org.jmailen.kotlinter")

    group = "org.katan"
    version = "0.0.1-SNAPSHOT"

    // some GH actions do not have OSSRH_SIGNING_KEY env var so we need to check it otherwise they will fail :(
    if (System.getenv("OSSRH_SIGNING_KEY") != null) {
        val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")
        publishOnCentral {
            configureMavenCentral.set(true)
            projectDescription.set("Multiplatform Docker API client")
            projectLongName.set(project.name)
            licenseName.set("MIT")
            licenseUrl.set("https://github.com/KatanPanel/yoki/blob/main/LICENSE")
            projectUrl.set("https://github.com/KatanPanel/yoki")
            scmConnection.set("git:git@github.com:KatanPanel/yoki")

            mavenCentral.user.set(
                (project.findProperty("ossrh.username") as String?) ?: System.getenv("OSSRH_USERNAME")
            )
            mavenCentral.password.set(provider {
                (project.findProperty("ossrh.password") as String?) ?: System.getenv("OSSRH_PASSWORD")
            })

            repository("https://maven.pkg.github.com/KatanPanel/yoki", "GitHub") {
                user.set(System.getenv("GITHUB_USERNAME"))
                password.set(System.getenv("GITHUB_TOKEN"))
            }

            if (!isReleaseVersion)
                mavenCentralSnapshotsRepository()
        }

        signing {
            useInMemoryPgpKeys(System.getenv("OSSRH_SIGNING_KEY"), System.getenv("OSSRH_SIGNING_PASSWORD"))
        }
    }
}

tasks.check {
    dependsOn("installKotlinterPrePushHook")
}