package gsonpath.generator.enums

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
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
