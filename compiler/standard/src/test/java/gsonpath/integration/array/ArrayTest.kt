package gsonpath.integration.array

import gsonpath.integration.common.GeneratorTester.assertGeneratedContent
import gsonpath.integration.common.TestCriteria
import org.junit.Test

class ArrayTest {

    @Test
    fun testArray() {
        assertGeneratedContent(TestCriteria("generator/standard/array",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("TestArray.java"),
                relativeGeneratedNames = listOf("TestArray_GsonTypeAdapter.java")
        ))
    }
}
