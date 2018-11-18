package gsonpath.generator.standard

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
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
