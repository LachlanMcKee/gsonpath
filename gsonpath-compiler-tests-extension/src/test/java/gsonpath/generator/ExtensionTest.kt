package gsonpath.generator

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import org.junit.Test

class ExtensionTest {

    @Test
    fun testExcludeFields() {
        assertGeneratedContent(TestCriteria("generator/standard",
                relativeSourceNames = listOf(
                        "TestGsonTypeFactory.java",
                        "TestExtension.java"),

                relativeGeneratedNames = listOf(
                        "TestExtension_GsonTypeAdapter.java")
        ))
    }
}
