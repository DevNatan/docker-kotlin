// https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
}

repositories {
    mavenCentral()
}

allprojects {
    group = "me.devnatan.yoki"
    version = "0.0.1"
}