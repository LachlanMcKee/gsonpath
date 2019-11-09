package gsonpath.generator.standard

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
import org.junit.Test

class CustomAutoGsonAdapterTest {
    @Test
    fun testCustomAutoGsonAdapterAnnotation() {
        assertGeneratedContent(TestCriteria("generator/standard/custom_adapter_annotation",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "CustomAutoGsonAdapter.java",
                        "TestCustomAutoGsonAdapterModel.java"),

                relativeGeneratedNames = listOf(
                        "TestCustomAutoGsonAdapterModel_GsonTypeAdapter.java")
        ), "-Agsonpath.addtionalAnnotations=generator.standard.custom_adapter_annotation.CustomAutoGsonAdapter")
    }
}
