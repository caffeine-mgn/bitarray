import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import pw.binom.publish.dependsOn

plugins {
    id("maven-publish")
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.binom.publish) apply false
    alias(libs.plugins.publish.central)
}

val jsRun = System.getProperty("jsrun") != null

fun KotlinMultiplatformExtension.eachNative(func: KotlinNativeTarget.() -> Unit) {
    this.targets.forEach {
        if (it is KotlinNativeTarget) {
            it.func()
        }
    }
}

allprojects {
//    val branch = getGitBranch()
    version = System.getenv("GITHUB_REF_NAME") ?: (property("version") as String?)?.takeIf { it != "unspecified" }
            ?: "1.0.0-SNAPSHOT"
    group = "pw.binom"

    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}

kotlin {
    jvm()
    js()
    linuxX64()
    linuxArm64()
    mingwX64()
    macosX64()
    macosArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    watchosX64()
    watchosArm32()
    watchosDeviceArm64()
    watchosSimulatorArm64()
    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX64()
    androidNativeX86()
    wasmJs()
    wasmWasi()
//    macosX64()
//    macosArm64()
//    ios()
//    iosArm64()
//    iosSimulatorArm64()
//    watchos()
//    watchosArm32()
//    watchosArm64()
//    watchosSimulatorArm64()
//    watchosX64()
//    tvos()
//    tvosArm64()
//    tvosSimulatorArm64()
//    tvosX64()
    /*
    jvm()
    linuxX64()
    linuxArm64()
    mingwX64()
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()
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
        js(IR) {
            browser()
            nodejs()
        }
    }
    */

//    allTargets{
//        -"wasmJs"
//    }
//    applyDefaultHierarchyTemplate()
//    applyDefaultHierarchyBinomTemplate()
    applyDefaultHierarchyTemplate()
    eachNative {
        compilations["main"].cinterops {
            create("bitarrayNative") {
                definitionFile.set(project.file("src/nativeCommonMain/c/bytearray.def"))
                packageName = "pw.binom.bitarray.native"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib"))
//                api("org.jetbrains.kotlin:kotlin-stdlib-common:${pw.binom.Versions.KOTLIN_VERSION}")
                api(kotlin("stdlib-common"))
            }
        }
        val fallbackMain by creating {
            dependsOn(commonMain)
        }
        dependsOn("jsMain", fallbackMain)
//        dependsOn("wasm32Main", fallbackMain)
        dependsOn("wasm*Main", fallbackMain)
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
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
        val nativeCommonMain by creating {
            dependsOn(nativeMain.get())
        }
        val nativeRunnableMain by creating {
            dependsOn(nativeCommonMain)
        }
        nativeMain.dependencies {

        }
        dependsOn("androidNative*", nativeRunnableMain)
        //useDefault()
    }
}

publishOnCentral {
    repoOwner.set("caffeine-mgn")
    projectLongName.set(project.name)
    licenseName.set("Apache License, Version 2.0")
    licenseUrl.set("http://www.apache.org/licenses/LICENSE-2.0")
    projectUrl.set("https://github.com/${repoOwner.get()}/${project.name}")
    scmConnection.set("scm:git:https://github.com/${repoOwner.get()}/${project.name}")
}

//apply<pw.binom.publish.plugins.PrepareProject>()
