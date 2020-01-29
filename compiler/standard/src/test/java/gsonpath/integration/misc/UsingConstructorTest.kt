package gsonpath.integration.misc

import gsonpath.integration.common.GeneratorTester.assertGeneratedContent
import gsonpath.integration.common.TestCriteria
import org.junit.Test

class UsingConstructorTest {
    @Test
    fun testDataClassJavaRepresentation() {
        assertGeneratedContent(TestCriteria("generator/standard/using_constructor/valid",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "ValidConstructorTest.java"),

                relativeGeneratedNames = listOf(
                        "ValidConstructorTest_GsonTypeAdapter.java")
        ))
    }
}
