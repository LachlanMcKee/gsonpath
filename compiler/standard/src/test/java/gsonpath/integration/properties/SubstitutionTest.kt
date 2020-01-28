package gsonpath.integration.properties

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourcesSubjectFactory
import gsonpath.GsonProcessor
import gsonpath.integration.GeneratorTester.assertGeneratedContent
import gsonpath.integration.TestCriteria
import org.junit.Test

class SubstitutionTest {
    @Test
    fun testValidSubstitution() {
        assertGeneratedContent(TestCriteria("generator/standard/substitution/valid",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestSubstitutionBase.java",
                        "TestSubstitutionImpl1.java",
                        "TestSubstitutionImpl2.java"),

                relativeGeneratedNames = listOf(
                        "TestSubstitutionImpl1_GsonTypeAdapter.java",
                        "TestSubstitutionImpl2_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testSubstitutionError_duplicateKeys() {
        val source = JavaFileObjects.forResource("generator/standard/substitution/errors/TestSubstitutionError_DuplicateKeys.java")

        assertAbout(JavaSourcesSubjectFactory.javaSources())
                .that(listOf(
                        JavaFileObjects.forResource("generator/standard/TestGsonTypeFactory.java"),
                        source
                ))
                .processedWith(GsonProcessor())
                .failsToCompile()
                .withErrorContaining("PathSubstitution original values must be unique")
                .`in`(source)
    }


}
