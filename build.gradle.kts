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
        this.forEach {
            println("--->${it.name}")
        }
        this.forEach {
            if (it.name in listOf(
                    "iosArm32Main",
                    "iosArm64Main",
                    "iosMain",
                    "iosSimulatorArm64Main",
                    "iosX64Main",
                    "macosArm64Main",
                    "macosX64Main",
                    "watchosArm32Main",
                    "watchosArm64Main",
                    "watchosDeviceMain",
                    "watchosMain",
                    "watchosSimulatorArm64Main",
                    "watchosX64Main",
                    "watchosX86Main"
                )
            ) {
                it.dependsOn(commonMain)
            }
            if (it.name in listOf(
                    "iosArm32Test",
                    "iosArm64Test",
                    "iosSimulatorArm64Test",
                    "iosTest",
                    "iosX64Test",
                    "macosArm64Test",
                    "macosX64Test",
                    "watchosArm32Test",
                    "watchosArm64Test",
                    "watchosDeviceTest",
                    "watchosSimulatorArm64Test",
                    "watchosTest",
                    "watchosX64Test",
                    "watchosX86Test"
                )){
                it.dependsOn(commonTest)
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

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    this.dokkaSourceSets {
        println("Names: ${dokkaSourceSets.names}")
//        named("main") {
//            println("this.name=${this.name}")
//        }
    }
}