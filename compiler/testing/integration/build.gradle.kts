plugins {
    java
    kotlin("jvm") version ProjectProperties.Versions.KOTLIN_VERSION
    jacoco
}

jacoco {
    toolVersion = ProjectProperties.Versions.JACOCO_VERSION
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":library"))
    implementation(project(":compiler:base"))
    implementation(project(":compiler:standard"))
    implementation(project(":compiler:testing:base"))

    implementation(ProjectProperties.Dependencies.KOTLIN_STD_LIB)
    implementation("com.google.truth:truth:0.34")
    implementation("com.google.testing.compile:compile-testing:0.11")
    testImplementation(ProjectProperties.Dependencies.JUNIT)
}
