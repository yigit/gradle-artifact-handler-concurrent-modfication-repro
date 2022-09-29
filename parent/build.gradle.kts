plugins {
    kotlin("jvm")
    id("com.birbit.parent")
}

dependencies {
    myParentConfiguration(project(":child"))
}
