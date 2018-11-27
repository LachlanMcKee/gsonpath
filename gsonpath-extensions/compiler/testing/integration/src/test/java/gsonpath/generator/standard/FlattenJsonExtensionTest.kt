package gsonpath.generator.standard

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import gsonpath.GsonProcessorImpl
import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
import org.junit.Test

class FlattenJsonExtensionTest {
    @Test
    fun testFlattenJsonInvalidType() {
        val source = JavaFileObjects.forResource("generator/standard/flatten/invalid/TestInvalidFlattenJson.java")

        assertAbout(javaSource()).that(source)
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining("FlattenObject can only be used on String variables")
                .`in`(source)
                .onLine(9)
    }

    @Test
    fun testFlattenJsonMutable() {
        assertGeneratedContent(TestCriteria("generator/standard/flatten/valid/mutable",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("TestMutableFlattenJson.java"),
                relativeGeneratedNames = listOf("TestMutableFlattenJson_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testFlattenJsonImmutable() {
        assertGeneratedContent(TestCriteria("generator/standard/flatten/valid/immutable",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("TestImmutableFlattenJson.java"),
                relativeGeneratedNames = listOf("TestImmutableFlattenJson_GsonTypeAdapter.java")
        ))
    }
}
