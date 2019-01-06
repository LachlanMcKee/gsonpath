package gsonpath.generator.extension

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
import org.junit.Test

class RemoveInvalidElementsTest {
    @Test
    fun testRemoveInvalidElementsMutable() {
        assertGeneratedContent(TestCriteria("generator/extension/invalid/mutable",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("TestMutableRemoveInvalidElements.java"),
                relativeGeneratedNames = listOf("TestMutableRemoveInvalidElements_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testRemoveInvalidElementsImmutable() {
        assertGeneratedContent(TestCriteria("generator/extension/invalid/immutable",
                absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                relativeSourceNames = listOf("TestImmutableRemoveInvalidElements.java"),
                relativeGeneratedNames = listOf("TestImmutableRemoveInvalidElements_GsonTypeAdapter.java")
        ))
    }
}
