package gsonpath.integration

import gsonpath.integration.GeneratorTester.assertGeneratedContent
import org.junit.Test

class NestedJsonTest {
    @Test
    fun testFlatteningUsingFields() {
        assertGeneratedContent(TestCriteria("generator/standard/nested_json/field_nesting",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestFieldNesting.java"),

                relativeGeneratedNames = listOf(
                        "TestFieldNesting_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testFlatteningUsingRootField() {
        assertGeneratedContent(TestCriteria("generator/standard/nested_json/root_nesting",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestRootNesting.java"),

                relativeGeneratedNames = listOf(
                        "TestRootNesting_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testFlatteningUsingFieldAutoComplete() {
        assertGeneratedContent(TestCriteria("generator/standard/nested_json/field_nesting_autocomplete",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestFieldNestingAutocomplete.java"),

                relativeGeneratedNames = listOf(
                        "TestFieldNestingAutocomplete_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun testFlatteningUsingFieldAutoCompleteInheritance() {
        assertGeneratedContent(TestCriteria("generator/standard/nested_json/field_nesting_autocomplete_inheritance",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java"),

                relativeSourceNames = listOf(
                        "TestFieldNestingAutocompleteBase.java",
                        "TestFieldNestingAutocomplete.java"),

                relativeGeneratedNames = listOf(
                        "TestFieldNestingAutocomplete_GsonTypeAdapter.java")
        ))
    }
}
