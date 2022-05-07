import pw.binom.getGitBranch
import pw.binom.publish.*

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("maven-publish")
}

val jsRun = System.getProperty("jsrun") != null

allprojects {
    val branch = getGitBranch()
    version = System.getenv("GITHUB_REF_NAME") ?: propertyOrNull("version") ?: "1.0.0-SNAPSHOT"
    group = "pw.binom"

    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}

kotlin {
    ifNotMac {
        jvm()
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
            js(BOTH) {
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
            val jsMain by getting {
                dependencies {
                    api(kotlin("stdlib-js"))
                }
            }

            val jsTest by getting {
                dependencies {
                    api(kotlin("test-js"))
                }
            }
            val jvmMain by getting {
                dependencies {
                    api("org.jetbrains.kotlin:kotlin-stdlib:${pw.binom.Versions.KOTLIN_VERSION}")
                    api("org.luaj:luaj-jse:3.0.1")
                }
            }

            val jvmTest by getting {
                dependencies {
                    api(kotlin("test-junit"))
                }
            }
        }
//        val macosX64Main by getting {
//            dependsOn(commonMain)
//        }
//        val macosArm64Main by getting {
//            dependsOn(commonMain)
//        }
//        val iosMain by getting {
//            dependsOn(commonMain)
//        }
//        val iosArm32Main by getting {
//            dependsOn(commonMain)
//        }
//        val iosArm64Main by getting {
//            dependsOn(commonMain)
//        }
//        val iosSimulatorArm64Main by getting {
//            dependsOn(commonMain)
//        }
//        val watchosMain by getting {
//            dependsOn(commonMain)
//        }
//        val watchosArm32Main by getting {
//            dependsOn(commonMain)
//        }
//        val watchosArm64Main by getting {
//            dependsOn(commonMain)
//        }
//        val watchosSimulatorArm64Main by getting {
//            dependsOn(commonMain)
//        }
//        val watchosX64Main by getting {
//            dependsOn(commonMain)
//        }
//        val watchosX86Main by getting {
//            dependsOn(commonMain)
//        }
    }
}

apply<pw.binom.publish.plugins.PrepareProject>()
