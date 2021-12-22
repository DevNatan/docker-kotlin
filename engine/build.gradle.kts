plugins {
    kotlin("multiplatform")
}

group = "org.katan"
version = "0.1.0"

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
                implementation(project(":api"))
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