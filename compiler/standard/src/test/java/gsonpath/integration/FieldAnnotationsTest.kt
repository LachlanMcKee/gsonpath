package gsonpath.integration

import gsonpath.integration.GeneratorTester.assertGeneratedContent
import org.junit.Test

class FieldAnnotationsTest {

    @Test
    fun testExcludeFields() {
        assertGeneratedContent(TestCriteria("generator/standard/field_annotations/exclude",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestExclude.java"),

                relativeGeneratedNames = listOf(
                        "TestExclude_GsonTypeAdapter.java")
        ))
    }
}
