package gsonpath.adapter.subType

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
import org.junit.Test

class SubTypeTest {

    @Test
    fun testDirectlyAnnotatedSubType() {
        assertGeneratedContent(TestCriteria("generator/gson_sub_type/directly_annotated",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("DirectlyAnnotatedSubType.java"),
                relativeGeneratedNames = listOf("DirectlyAnnotatedSubType_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testIndirectlyAnnotatedSubType() {
        assertGeneratedContent(TestCriteria("generator/gson_sub_type/indirectly_annotated",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("IndirectlyAnnotatedSubType.java"),
                relativeGeneratedNames = listOf("IndirectlyAnnotatedSubType_GsonTypeAdapter.java")
        ))
    }
}
