package gsonpath.integration.misc

import gsonpath.integration.common.GeneratorTester.assertGeneratedContent
import gsonpath.integration.common.TestCriteria
import org.junit.Test

class InheritanceTest {
    @Test
    fun testInheritance() {
        assertGeneratedContent(TestCriteria("generator/standard/inheritance",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestInheritanceBase.java",
                        "TestInheritance.java"),

                relativeGeneratedNames = listOf(
                        "TestInheritance_GsonTypeAdapter.java")
        ))
    }
}
