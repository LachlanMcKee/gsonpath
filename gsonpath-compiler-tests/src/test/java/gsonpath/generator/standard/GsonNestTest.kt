package gsonpath.generator.standard

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
import org.junit.Test

class GsonNestTest {
    @Test
    fun testCustomAutoGsonAdapterAnnotation() {
        assertGeneratedContent(TestCriteria("generator/standard/custom_serialized_name_annotation",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "GsonNest.java",
                        "TestCustomSerializedNameModel.java"),

                relativeGeneratedNames = listOf(
                        "TestCustomSerializedNameModel_GsonTypeAdapter.java")
        ))
    }
}
