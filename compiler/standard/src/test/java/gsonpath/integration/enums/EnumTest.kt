package gsonpath.integration.enums

import gsonpath.integration.common.GeneratorTester.assertGeneratedContent
import gsonpath.integration.common.TestCriteria
import org.junit.Test

class EnumTest {

    @Test
    fun testEnum() {
        assertGeneratedContent(TestCriteria("generator/enums",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("TestEnum.java"),
                relativeGeneratedNames = listOf("TestEnum_GsonTypeAdapter.java")
        ))
    }
}
