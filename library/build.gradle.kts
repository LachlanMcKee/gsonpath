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
    id("com.vanniktech.maven.publish") version "0.7.0"
}

java {
    targetCompatibility = JavaVersion.VERSION_1_7
    sourceCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation(fileTree("libs").include("*.jar"))
    implementation(ProjectProperties.Dependencies.GSON)
}
