import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import pw.binom.publish.dependsOn
import pw.binom.publish.propertyOrNull
import pw.binom.publish.useDefault

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("maven-publish")
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
    iosArm64()
    iosSimulatorArm64()
    watchos()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
    watchosX64()
    tvos()
    tvosArm64()
    tvosSimulatorArm64()
    tvosX64()
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
    eachNative {
        compilations["main"].cinterops {
            create("bitarrayNative") {
                defFile = project.file("src/nativeCommonMain/c/bytearray.def")
                packageName = "pw.binom.bitarray.native"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib"))
                api("org.jetbrains.kotlin:kotlin-stdlib-common:${pw.binom.Versions.KOTLIN_VERSION}")
            }
        }
        val fallbackMain by creating {
            dependsOn(commonMain)
        }
        dependsOn("jsMain", fallbackMain)
        dependsOn("wasm32Main", fallbackMain)
        dependsOn("wasmMain", fallbackMain)
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
        useDefault()
    }
}
apply<pw.binom.publish.plugins.PrepareProject>()
