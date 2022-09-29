plugins {
    kotlin("jvm")
    id("com.birbit.parent")
}

dependencies {
    myParentConfiguration(project(":child"))
    myParentConfiguration(project(":child2"))
}
