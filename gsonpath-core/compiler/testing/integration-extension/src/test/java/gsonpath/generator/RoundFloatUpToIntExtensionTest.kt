package gsonpath.generator

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import org.junit.Test

class RoundFloatUpToIntExtensionTest {

    @Test
    fun testRoundFloatUpToIntObject() {
        assertGeneratedContent(TestCriteria("generator/standard",
                relativeSourceNames = listOf(
                        "TestGsonTypeFactory.java",
                        "RoundFloatUpToIntObject.java"),

                relativeGeneratedNames = listOf(
                        "RoundFloatUpToIntObject_GsonTypeAdapter.java")
        ))
    }
}
