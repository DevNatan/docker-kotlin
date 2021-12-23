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
    jvm()
    linuxX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(project(":api"))
                implementation(project(":engine"))
                implementation(libs.bundles.ktor)
                implementation(libs.bundles.ktx)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.ktx.coroutines.test)
            }
        }
    }
}