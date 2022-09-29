import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10" apply false
}

repositories {
    mavenCentral()
}

allprojects {
    repositories {
        mavenCentral()
    }
}