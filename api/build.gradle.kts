plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "org.katan"
version = "0.1.0"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    linuxX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}