buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven(url = "https://repo.binom.pw")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
    }
}

plugins {
    kotlin("jvm") version "1.7.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://repo.binom.pw")
    maven(url = "https://plugins.gradle.org/m2/")
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:1.7.0")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
//    api("org.jetbrains.dokka:dokka-gradle-plugin:1.7.0")
    api("pw.binom:binom-publish:0.1.0")
}


