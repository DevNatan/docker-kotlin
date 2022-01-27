// https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    id("org.danilopianini.publish-on-central") version "0.7.11" // TODO version catalog
    id("org.jmailen.kotlinter") version "3.8.0" // TODO version catalog
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.danilopianini.publish-on-central")
    apply(plugin = "org.jmailen.kotlinter")

    group = "org.katan"
    version = "0.0.1"

    publishOnCentral {
        configureMavenCentral.set(true)
        projectLongName.set(project.name)
        licenseName.set("MIT")
        licenseUrl.set("https://github.com/KatanPanel/${project.name}/blob/main/LICENSE")
        projectUrl.set("https://github.com/KatanPanel/${project.name}")
        scmConnection.set("git:git@github.com:KatanPanel/${project.name}")

        mavenCentral.user.set(System.getenv("OSSRH_USERNAME"))
        mavenCentral.password.set(provider { System.getenv("OSSRH_PASSWORD") })

        repository("https://maven.pkg.github.com/KatanPanel/yoki", "GitHub") {
            user.set(System.getenv("GITHUB_USERNAME"))
            password.set(System.getenv("GITHUB_TOKEN"))
        }

        if (project.version.toString().endsWith("-SNAPSHOT"))
            mavenCentralSnapshotsRepository()
    }
}

tasks.check {
    dependsOn("installKotlinterPrePushHook")
}
