package gsonpath.integration

import gsonpath.integration.GeneratorTester.assertGeneratedContent
import org.junit.Test

class GenericsTest {
    @Test
    fun testUseInheritance() {
        assertGeneratedContent(TestCriteria("generator/standard/generics/interfaces",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "BaseTest.java",
                        "GenericsTest.java"),

                relativeGeneratedNames = listOf(
                        "GenericsTest_GsonPathModel.java",
                        "GenericsTest_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testUseClass() {
        assertGeneratedContent(TestCriteria("generator/standard/generics/classes",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "BaseTest.java",
                        "IntermediateTest.java",
                        "GenericsTest.java"),

                relativeGeneratedNames = listOf(
                        "GenericsTest_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testUseInterfaceAndClass() {
        assertGeneratedContent(TestCriteria("generator/standard/generics/interfaces_and_classs",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "BaseTest.java",
                        "IntermediateTest.java",
                        "GenericsTest.java"),

                relativeGeneratedNames = listOf(
                        "GenericsTest_GsonTypeAdapter.java")
        ))
    }
}
