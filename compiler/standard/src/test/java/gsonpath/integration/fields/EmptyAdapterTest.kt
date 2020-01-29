package gsonpath.integration.fields

import gsonpath.integration.common.GeneratorTester.assertGeneratedContent
import gsonpath.integration.common.TestCriteria
import org.junit.Test

/**
 * These tests are used whenever the input class file generates a blank type adapter.
 *
 *
 * This can be due to the class being empty, or the content within the class isn't
 * applicable for a type adapter.
 */
class EmptyAdapterTest {

    /**
     * Tests the output generated when only a [gsonpath.AutoGsonAdapter] annotation is used.
     */
    @Test
    fun testAutoGsonAdapterOnly() {
        assertGeneratedContent(TestCriteria("generator/standard/empty/annotation_only",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestAnnotationOnly.java"),

                relativeGeneratedNames = listOf(
                        "TestAnnotationOnly_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testRequiresAnnotation() {
        assertGeneratedContent(TestCriteria("generator/standard/empty/ignored_fields",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestIgnoredFields.java"),

                relativeGeneratedNames = listOf(
                        "TestIgnoredFields_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testIgnoreInvalidFields() {
        assertGeneratedContent(TestCriteria("generator/standard/empty/invalid_fields",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestInvalidFields.java"),

                relativeGeneratedNames = listOf(
                        "TestInvalidFields_GsonTypeAdapter.java")
        ))
    }
}
