plugins {
    java
    kotlin("jvm") version ProjectProperties.Versions.KOTLIN_VERSION
    kotlin("kapt") version ProjectProperties.Versions.KOTLIN_VERSION
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(ProjectProperties.Dependencies.KOTLIN_STD_LIB)
    implementation(ProjectProperties.Dependencies.GSON)

    implementation(project(":library"))
    kapt(project(":compiler:standard"))

    testImplementation(ProjectProperties.Dependencies.JUNIT)
}