package gsonpath.integration

import gsonpath.integration.GeneratorTester.assertGeneratedContent
import org.junit.Test

class FieldTypesTest {
    @Test
    fun testValidPrimitives() {
        assertGeneratedContent(TestCriteria("generator/standard/field_types/primitives/valid",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestValidPrimitives.java"),

                relativeGeneratedNames = listOf(
                        "TestValidPrimitives_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testBoxedPrimitives() {
        assertGeneratedContent(TestCriteria("generator/standard/field_types/boxed_primitives",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestBoxedPrimitives.java"),

                relativeGeneratedNames = listOf(
                        "TestBoxedPrimitives_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testGenericFields() {
        assertGeneratedContent(TestCriteria("generator/standard/field_types/generics",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestGenerics.java"),

                relativeGeneratedNames = listOf(
                        "TestGenerics_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testCustomFieldType() {
        assertGeneratedContent(TestCriteria("generator/standard/field_types/custom_field",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestCustomField.java"),

                relativeGeneratedNames = listOf(
                        "TestCustomField_GsonTypeAdapter.java")
        ))
    }

}
