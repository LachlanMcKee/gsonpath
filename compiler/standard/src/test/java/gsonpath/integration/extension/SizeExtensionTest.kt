package gsonpath.integration.extension

import gsonpath.integration.common.GeneratorTester.assertGeneratedContent
import gsonpath.integration.common.TestCriteria
import org.junit.Test

class SizeExtensionTest {
    @Test
    fun testSizeMutable() {
        assertGeneratedContent(TestCriteria("generator/extension/size/valid/mutable",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("TestMutableSize.java"),
                relativeGeneratedNames = listOf("TestMutableSize_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testSizeImmutable() {
        assertGeneratedContent(TestCriteria("generator/extension/size/valid/immutable",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("TestImmutableSize.java"),
                relativeGeneratedNames = listOf("TestImmutableSize_GsonTypeAdapter.java")
        ))
    }
}
