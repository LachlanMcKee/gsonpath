import ProjectProperties.Versions.GSON_VERSION
import ProjectProperties.Versions.JAVAPOET_VERSION
import ProjectProperties.Versions.JUNIT_VERSION
import ProjectProperties.Versions.KOTLIN_VERSION

object ProjectProperties {
    object Versions {
        const val KOTLIN_VERSION = "1.3.11"
        const val JAVAPOET_VERSION = "1.11.1"
        const val GSON_VERSION = "2.8.1"
        const val JUNIT_VERSION = "4.12"
        const val JACOCO_VERSION = "0.8.2"
    }

    object Dependencies {
        const val KOTLIN_STD_LIB = "org.jetbrains.kotlin:kotlin-stdlib:$KOTLIN_VERSION"
        const val JAVAPOET = "com.squareup:javapoet:$JAVAPOET_VERSION"
        const val JUNIT = "junit:junit:$JUNIT_VERSION"
        const val GSON = "com.google.code.gson:gson:$GSON_VERSION"
    }


    const val PROJECT_GROUP = "net.lachlanmckee"
    const val CORE_VERSION = "3.1.0"
    const val COMPILER_BASE_VERSION = "1.3.0"
    const val EXTENSIONS_VERSION = "1.2.0"
}