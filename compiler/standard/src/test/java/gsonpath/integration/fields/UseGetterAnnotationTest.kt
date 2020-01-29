package gsonpath.integration.fields

import gsonpath.integration.common.GeneratorTester.assertGeneratedContent
import gsonpath.integration.common.TestCriteria
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
