buildscript {
    repositories {
        jcenter()
    }
}

plugins {
    id("org.sonarqube") version "2.6.2"
    jacoco
}

jacoco {
    toolVersion = ProjectProperties.Versions.JACOCO_VERSION
}

allprojects {
    repositories {
        jcenter()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

sonarqube {
    properties {
        property("sonar.projectName", "Gsonpath")
        property("sonar.projectKey", "gsonpath")
    }
}

task<JacocoReport>("integrationCodeCoverageReport") {
    executionData.setFrom(fileTree("compiler/testing/integration").include("**/build/jacoco/*.exec"))

    // Ignore extension code
    val excludes = "gsonpath/compiler/ExtensionFieldMetadata.class"

    classDirectories.setFrom(fileTree("compiler/standard/build/classes/java/main") +
            fileTree("compiler/standard/build/classes/kotlin/main") +
            fileTree("compiler/base/build/classes/java/main").exclude(excludes) +
            fileTree("compiler/base/build/classes/kotlin/main").exclude(excludes))

    sourceDirectories.setFrom(files("compiler/standard/src/main/java",
            "compiler/base/src/main/java"))

    reports {
        xml.isEnabled = true
        xml.destination = File("$buildDir/reports/jacoco/report.xml")
        html.isEnabled = false
        csv.isEnabled = false
    }

    dependsOn(":compiler:testing:integration:test")
}

task<JacocoReport>("unitCodeCoverageReport") {
    executionData.setFrom(fileTree("compiler/testing/unit").include("**/build/jacoco/*.exec"))

    // Ignore extension code
    val excludes = "gsonpath/compiler/ExtensionFieldMetadata.class"

    classDirectories.setFrom(fileTree("compiler/standard/build/classes/java/main") +
            fileTree("compiler/standard/build/classes/kotlin/main") +
            fileTree("compiler/base/build/classes/java/main").exclude(excludes) +
            fileTree("compiler/base/build/classes/kotlin/main").exclude(excludes))

    sourceDirectories.setFrom(files("compiler/standard/src/main/java",
            "compiler/base/src/main/java"))

    reports {
        xml.isEnabled = true
        xml.destination = File("$buildDir/reports/jacoco/report.xml")
        html.isEnabled = false
        csv.isEnabled = false
    }

    dependsOn(":compiler:testing:unit:test")
}
