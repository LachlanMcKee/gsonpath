package gsonpath.integration.properties

import gsonpath.integration.GeneratorTester.assertGeneratedContent
import gsonpath.integration.TestCriteria
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
