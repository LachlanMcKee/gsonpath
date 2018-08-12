package gsonpath.generator.standard

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
import org.junit.Test

class UseGetterAnnotationTest {
    @Test
    fun testDataClassJavaRepresentation() {
        assertGeneratedContent(TestCriteria("generator/standard/use_getter_annotation/",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "UseGetterAnnotationTest.java"),

                relativeGeneratedNames = listOf(
                        "UseGetterAnnotationTest_Implementation_GsonTypeAdapter.java")
        ))
    }
}
