package gsonpath.generator

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import org.junit.Test

class EmptyStringToNullExtensionTest {

    @Test
    fun testEmptyStringToNull() {
        assertGeneratedContent(TestCriteria("generator/standard",
                relativeSourceNames = listOf(
                        "TestGsonTypeFactory.java",
                        "EmptyStringToNullObject.java"),

                relativeGeneratedNames = listOf(
                        "EmptyStringToNullObject_GsonTypeAdapter.java")
        ))
    }
}
