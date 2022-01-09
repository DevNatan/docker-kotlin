pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "yoki"
include("yoki-api")
include("yoki-engine")
include("yoki-engine-docker")
include("yoki-protocol")
