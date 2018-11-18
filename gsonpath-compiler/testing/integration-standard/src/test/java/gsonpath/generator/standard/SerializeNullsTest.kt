package gsonpath.generator.standard

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
import org.junit.Test

class SerializeNullsTest {

    @Test
    fun testSerializeNulls() {
        assertGeneratedContent(TestCriteria("generator/standard/serialize_nulls",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestSerializeNulls.java"),

                relativeGeneratedNames = listOf(
                        "TestSerializeNulls_GsonTypeAdapter.java")
        ))
    }

}
