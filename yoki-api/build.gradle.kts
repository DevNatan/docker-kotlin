plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    jvm()

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

        val jvmMain by getting {
            dependsOn(commonMain)
        }

        val jvmTest by getting {
            dependsOn(commonTest)
        }
    }
}