package gsonpath.integration.array

import gsonpath.integration.GeneratorTester.assertGeneratedContent
import gsonpath.integration.TestCriteria
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
