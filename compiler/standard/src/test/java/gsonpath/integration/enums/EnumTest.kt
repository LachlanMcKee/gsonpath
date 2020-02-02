package gsonpath.integration.enums

import gsonpath.integration.common.GeneratorTester.assertGeneratedContent
import gsonpath.integration.common.TestCriteria
import org.junit.Test

class EnumTest {

    @Test
    fun testEnumWithoutDefault() {
        assertGeneratedContent(TestCriteria("generator/enums/without_default",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("TestEnumWithoutDefault.java"),
                relativeGeneratedNames = listOf("TestEnumWithoutDefault_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testEnumWithDefault() {
        assertGeneratedContent(TestCriteria("generator/enums/with_default",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("TestEnumWithDefault.java"),
                relativeGeneratedNames = listOf("TestEnumWithDefault_GsonTypeAdapter.java")
        ))
    }
}
