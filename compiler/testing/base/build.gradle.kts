plugins {
    java
    kotlin("jvm") version ProjectProperties.Versions.KOTLIN_VERSION
}

java {
    targetCompatibility = JavaVersion.VERSION_1_7
    sourceCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation(project(":compiler:base"))
    implementation(ProjectProperties.Dependencies.KOTLIN_STD_LIB)
    implementation(ProjectProperties.Dependencies.JUNIT)
}
