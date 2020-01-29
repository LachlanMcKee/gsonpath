package gsonpath.integration.nesting

import gsonpath.integration.common.GeneratorTester.assertGeneratedContent
import gsonpath.integration.common.TestCriteria
import org.junit.Test

class NestedClassTest {
    @Test
    fun testNestedClasses() {
        assertGeneratedContent(TestCriteria("generator/standard/nested_class",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestNestedClass.java"),

                relativeGeneratedNames = listOf(
                        "TestNestedClass_Nested_GsonTypeAdapter.java")
        ))
    }
}
