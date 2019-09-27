import org.gradle.internal.jvm.Jvm

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.7.0")
    }
}

plugins {
    java
    kotlin("jvm") version ProjectProperties.Versions.KOTLIN_VERSION
    id("com.vanniktech.maven.publish") version "0.7.0"
}

java {
    targetCompatibility = JavaVersion.VERSION_1_7
    sourceCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation(ProjectProperties.Dependencies.KOTLIN_STD_LIB)

    implementation(project(":library"))
    implementation(project(":compiler:base"))

    implementation(ProjectProperties.Dependencies.JAVAPOET)
    implementation("com.google.auto:auto-common:0.6")
    implementation(ProjectProperties.Dependencies.GSON)
    implementation(files(Jvm.current().getToolsJar()))
}
