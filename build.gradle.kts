plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jmailen.kotlinter")
}

apply<pw.binom.plugins.BinomPublishPlugin>()
val jsRun = System.getProperty("jsrun") != null
kotlin {
    jvm()
    linuxX64()
    linuxArm32Hfp()
    linuxArm64()
    mingwX64()
    mingwX86()
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()
    macosX64()
    wasm32()
    if (pw.binom.Config.JS_TARGET_SUPPORT) {
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
                browser {
                    browser {
                        testTask {
                            if (!applled) {
                                applled = true
                                useKarma {
                                    useChromiumHeadless()
//                                useFirefoxHeadless()
                                }
                            }
                        }
                    }
                }
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
        if (pw.binom.Config.JS_TARGET_SUPPORT) {
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
}

allprojects {
    version = pw.binom.Versions.LIB_VERSION
    group = "pw.binom"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}
kotlinter {
    indentSize = 4
    disabledRules = arrayOf("no-wildcard-imports")
}
apply<pw.binom.plugins.DocsPlugin>()