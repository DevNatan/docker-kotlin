plugins {
    kotlin("multiplatform")
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
                implementation(project(":yoki-api"))
                api(project(":yoki-protocol"))
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