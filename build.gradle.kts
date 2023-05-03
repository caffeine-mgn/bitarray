import pw.binom.publish.ifNotMac
import pw.binom.publish.propertyOrNull
import pw.binom.publish.useDefault

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("maven-publish")
}

val jsRun = System.getProperty("jsrun") != null

allprojects {
//    val branch = getGitBranch()
    version = System.getenv("GITHUB_REF_NAME") ?: propertyOrNull("version")?.takeIf { it != "unspecified" }
        ?: "1.0.0-SNAPSHOT"
    group = "pw.binom"

    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}

kotlin {
    macosX64()
    macosArm64()
    ios()
    iosArm32()
    iosArm64()
    iosSimulatorArm64()
    watchos()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
    watchosX64()
    watchosX86()
    jvm()
    ifNotMac {
        linuxX64()
        linuxArm32Hfp()
        linuxArm64()
        linuxMips32()
        linuxMipsel32()
        mingwX64()
        mingwX86()
        androidNativeArm32()
        androidNativeArm64()
        androidNativeX86()
        androidNativeX64()
        wasm32()
        wasm {
            browser()
            binaries.executable()
        }
        if (jsRun) {
            js("js") {
                browser {
                    testTask {
                        useKarma {
                            useFirefox()
//                        useFirefoxHeadless()
//                        useChromium()
                        }
                    }
                }
                binaries.executable()
            }
        } else {
            var applled = false
            js(IR) {
                browser()
                nodejs()
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlin:kotlin-stdlib-common:${pw.binom.Versions.KOTLIN_VERSION}")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        ifNotMac {

            val jsTest by getting {
                dependencies {
                    api(kotlin("test-js"))
                }
            }

            val jvmTest by getting {
                dependencies {
                    api(kotlin("test-junit"))
                }
            }
        }
        useDefault()
    }
}
apply<pw.binom.publish.plugins.PrepareProject>()
