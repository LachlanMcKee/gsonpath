package gsonpath.generator.standard

import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
import org.junit.Test

class FieldNamingPolicyTest {
    @Test
    fun testIdentity() {
        assertGeneratedContent(TestCriteria("generator/standard/naming_policy/identity",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestNamePolicyIdentity.java"),

                relativeGeneratedNames = listOf(
                        "TestNamePolicyIdentity_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testLowerCaseWithDashes() {
        assertGeneratedContent(TestCriteria("generator/standard/naming_policy/lowercase_dashes",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestNamePolicyLowerCaseDashes.java"),

                relativeGeneratedNames = listOf(
                        "TestNamePolicyLowerCaseDashes_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testLowerCaseWithUnderscores() {
        assertGeneratedContent(TestCriteria("generator/standard/naming_policy/lowercase_underscores",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestNamePolicyLowerCaseUnderscores.java"),

                relativeGeneratedNames = listOf(
                        "TestNamePolicyLowerCaseUnderscores_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testUpperCamelCase() {
        assertGeneratedContent(TestCriteria("generator/standard/naming_policy/upper_camel_case",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestNamePolicyUpperCamelCase.java"),

                relativeGeneratedNames = listOf(
                        "TestNamePolicyUpperCamelCase_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testUpperCamelCaseWithSpaces() {
        assertGeneratedContent(TestCriteria("generator/standard/naming_policy/upper_camel_case_spaces",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestNamePolicyUpperCamelCaseSpaces.java"),

                relativeGeneratedNames = listOf(
                        "TestNamePolicyUpperCamelCaseSpaces_GsonTypeAdapter.java")
        ))
    }
}
