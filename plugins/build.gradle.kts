plugins {
    kotlin("jvm") version "1.7.10"
    id("java-gradle-plugin")
}

dependencies {
    implementation(gradleApi())
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("MyParentPlugin") {
            id = "com.birbit.parent"
            implementationClass = "MyParentPlugin"
        }
        create("MyChildPlugin") {
            id = "com.birbit.child"
            implementationClass = "MyChildPlugin"
        }
    }
}