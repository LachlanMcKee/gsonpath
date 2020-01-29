package gsonpath.integration.interf

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourcesSubjectFactory
import gsonpath.GsonProcessor
import gsonpath.integration.common.GeneratorTester.assertGeneratedContent
import gsonpath.integration.common.TestCriteria
import org.junit.Test

class InterfaceTest {
    @Test
    fun testValidInterface() {
        assertGeneratedContent(TestCriteria("generator/interf/valid",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestValidInterface.java"),

                relativeGeneratedNames = listOf(
                        "PackagePrivateTypeAdapterLoader.java",
                        "TestValidInterface_GsonPathModel.java",
                        "TestValidInterface_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testInvalidInterface_returningVoid() {
        val source = JavaFileObjects.forResource("generator/interf/invalid/TestValidInterface_ReturningVoid.java")
        assertAbout(JavaSourcesSubjectFactory.javaSources())
                .that(listOf(
                        JavaFileObjects.forResource("generator/standard/TestGsonTypeFactory.java"),
                        source
                ))
                .processedWith(GsonProcessor())
                .failsToCompile()
                .withErrorContaining("Gson Path interface methods must have a return type")
                .`in`(source)
                .onLine(7)
    }

    @Test
    fun testInvalidInterface_withParameters() {
        val source = JavaFileObjects.forResource("generator/interf/invalid/TestValidInterface_WithParameters.java")

        assertAbout(JavaSourcesSubjectFactory.javaSources())
                .that(listOf(
                        JavaFileObjects.forResource("generator/standard/TestGsonTypeFactory.java"),
                        source
                ))
                .processedWith(GsonProcessor())
                .failsToCompile()
                .withErrorContaining("Gson Path interface methods must not have parameters")
                .`in`(source)
                .onLine(7)
    }

    @Test
    fun testUsingPrimitives() {
        assertGeneratedContent(TestCriteria("generator/interf/primitive",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestUsingPrimitives.java"),

                relativeGeneratedNames = listOf(
                        "TestUsingPrimitives_GsonPathModel.java",
                        "TestUsingPrimitives_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testUsingInheritance() {
        assertGeneratedContent(TestCriteria("generator/interf/inheritance",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestUsingInheritanceBase.java",
                        "TestUsingInheritance.java"),

                relativeGeneratedNames = listOf(
                        "TestUsingInheritance_GsonPathModel.java",
                        "TestUsingInheritance_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testJava8Interface() {
        assertGeneratedContent(TestCriteria("generator/interf/java8",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestJava8Interface.java"),

                relativeGeneratedNames = listOf(
                        "TestJava8Interface_GsonPathModel.java",
                        "TestJava8Interface_GsonTypeAdapter.java")
        ))
    }
}
