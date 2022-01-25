buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven(url = "https://repo.binom.pw")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}

plugins {
    kotlin("jvm") version "1.6.10"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://repo.binom.pw")
    maven(url = "https://plugins.gradle.org/m2/")
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    api("org.jetbrains.dokka:dokka-gradle-plugin:1.6.0")
    api("org.jmailen.gradle:kotlinter-gradle:3.8.0")
//    api("com.android.tools.build:gradle:7.0.0")
}
