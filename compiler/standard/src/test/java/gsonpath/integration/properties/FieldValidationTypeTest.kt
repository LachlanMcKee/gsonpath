package gsonpath.integration.properties

import gsonpath.integration.common.GeneratorTester.assertGeneratedContent
import gsonpath.integration.common.TestCriteria
import org.junit.Test

class FieldValidationTypeTest {
    @Test
    fun testValidateExplicitNonNull() {
        assertGeneratedContent(TestCriteria("generator/standard/field_policy/validate_explicit_non_null",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestValidateExplicitNonNull.java"),

                relativeGeneratedNames = listOf(
                        "TestValidateExplicitNonNull_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testValidateAllExceptNullable() {
        assertGeneratedContent(TestCriteria("generator/standard/field_policy/validate_all_except_nullable",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestValidateAllExceptNullable.java"),

                relativeGeneratedNames = listOf(
                        "TestValidateAllExceptNullable_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testNoValidation() {
        assertGeneratedContent(TestCriteria("generator/standard/field_policy/no_validation",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestNoValidation.java"),

                relativeGeneratedNames = listOf(
                        "TestNoValidation_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testValidateWithDefaultValue() {
        assertGeneratedContent(TestCriteria("generator/standard/field_policy/default_value",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestValidateWithDefaultValue.java"),

                relativeGeneratedNames = listOf(
                        "TestValidateWithDefaultValue_GsonTypeAdapter.java")
        ))
    }
}