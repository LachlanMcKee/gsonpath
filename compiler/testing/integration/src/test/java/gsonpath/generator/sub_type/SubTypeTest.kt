package gsonpath.generator.sub_type

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
import org.junit.Test

class SubTypeTest {

    @Test
    fun testSubType() {
        assertGeneratedContent(TestCriteria("generator/gson_sub_type",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("Type.java"),
                relativeGeneratedNames = listOf("Type_GsonTypeAdapter.java")
        ))
    }
}
