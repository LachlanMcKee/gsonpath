package gsonpath.generator.extension

import com.google.common.truth.Truth
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourcesSubjectFactory
import gsonpath.GsonProcessorImpl
import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
import org.junit.Test

class GsonSubTypeExtensionTest {
    private fun test(packageName: String, className: String) {
        assertGeneratedContent(TestCriteria("generator/extension/gson_sub_type/$packageName",

                absoluteSourceNames = listOf(
                        "generator/standard/TestGsonTypeFactory.java",
                        "generator/extension/gson_sub_type/Type.java",
                        "generator/extension/gson_sub_type/Type1.java",
                        "generator/extension/gson_sub_type/Type2.java"),

                relativeSourceNames = listOf(
                        "$className.java"),

                relativeGeneratedNames = listOf(
                        "${className}_GsonTypeAdapter.java")
        ))
    }

    @Test
    fun givenStringKeys_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        test("string_keys", "TypesList")
    }

    @Test
    fun givenIntegerKeys_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        test("integer_keys", "TypesList")
    }

    @Test
    fun givenBooleanKeys_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        test("boolean_keys", "TypesList")
        test("boolean_keys", "TypesPojo")
    }

    @Test
    fun givenStringKeysWithInterface_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        test("using_interface", "TypesList")
    }

    @Test
    fun givenStringKeysWithListField_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        test("using_list", "TypesList")
    }

    @Test
    fun givenDefaultValueAndDefaultFailureOutcome_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        test("default_value", "TypesList")
    }

    @Test
    fun givenRemoveElementFailureOutcome_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        test("failure_outcome_remove_element", "TypesList")
    }

    @Test
    fun givenFailFailureOutcome_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        test("failure_outcome_fail", "TypesList")
    }

    @Test
    fun givenStringKeysAndNonPolymorphismElements_whenProcessorExecuted_expectValidGsonTypeAdapter() {
        test("with_other_elements", "TypesList")
    }

    @Test
    fun givenNoKeys_whenProcessorExecuted_expectNoKeysError() {
        assertPolymorphismFailure("TypesList_NoKeys.java",
                "Gson Path: Keys must be specified for the GsonSubType")
    }

    @Test
    fun givenMultipleKeys_whenProcessorExecuted_expectMultipleKeysError() {
        assertPolymorphismFailure("TypesList_MultipleKeys.java",
                "Only one keys array (string, integer or boolean) may be specified for the GsonSubType")
    }

    @Test
    fun givenBlankFieldName_whenProcessorExecuted_expectBlankFieldNameError() {
        assertPolymorphismFailure("TypesList_BlankFieldName.java",
                "Gson Path: subTypeKey cannot be blank for GsonSubType")
    }

    @Test
    fun givenNoInheritanceLink_whenProcessorExecuted_expectBlankFieldNameError() {
        assertPolymorphismFailure("TypesList_TypeInvalidInheritance.java",
                "Gson Path: subtype java.lang.String does not inherit from generator.extension.gson_sub_type.Type")
    }

    private fun assertPolymorphismFailure(className: String, errorMessage: String) {
        Truth.assertAbout(JavaSourcesSubjectFactory.javaSources())
                .that(listOf(
                        JavaFileObjects.forResource("generator/standard/TestGsonTypeFactory.java"),
                        JavaFileObjects.forResource("generator/extension/gson_sub_type/failures/$className")
                ))
                .processedWith(GsonProcessorImpl())
                .failsToCompile()
                .withErrorContaining(errorMessage)
    }
}