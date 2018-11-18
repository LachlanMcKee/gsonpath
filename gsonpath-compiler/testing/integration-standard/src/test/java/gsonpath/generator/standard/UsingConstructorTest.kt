package gsonpath.generator.standard

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
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
