plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    // TODO linuxX64 and macosX64
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
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
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.ktx.coroutines.test)
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
                compileOnly(libs.bundles.junixsocket)
                implementation(libs.ktor.client.okhttp)
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}