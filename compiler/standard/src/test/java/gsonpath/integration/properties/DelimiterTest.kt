package gsonpath.integration.properties

import gsonpath.integration.GeneratorTester.assertGeneratedContent
import gsonpath.integration.TestCriteria
import org.junit.Test

class DelimiterTest {

    @Test
    fun testStandardDelimiter() {
        assertGeneratedContent(TestCriteria("generator/standard/delimiter/standard",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestStandardDelimiter.java"),

                relativeGeneratedNames = listOf(
                        "TestStandardDelimiter_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testCustomDelimiter() {
        assertGeneratedContent(TestCriteria("generator/standard/delimiter/custom",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestCustomDelimiter.java"),

                relativeGeneratedNames = listOf(
                        "TestCustomDelimiter_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testMultipleDelimiters() {
        assertGeneratedContent(TestCriteria("generator/standard/delimiter/multiple",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestMultipleDelimiters.java"),

                relativeGeneratedNames = listOf(
                        "TestMultipleDelimiters_GsonTypeAdapter.java")
        ))
    }

}
