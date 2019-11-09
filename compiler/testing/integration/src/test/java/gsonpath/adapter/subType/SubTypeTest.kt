package gsonpath.adapter.subType

import com.google.common.truth.Truth
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourcesSubjectFactory
import gsonpath.GsonPathAdapterProcessor
import gsonpath.generator.GeneratorTester.assertGeneratedContent
import gsonpath.generator.TestCriteria
import org.junit.Test

class SubTypeTest {

    @Test
    fun testOneArgumentSubType() = test("one_argument")

    @Test
    fun testTwoArgumentsSubType() = test("two_arguments")

    @Test
    fun testIndirectlyAnnotatedSubType() =
            assertGeneratedContent(TestCriteria("generator/gson_sub_type/indirectly_annotated",
                    absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                    relativeSourceNames = listOf("IndirectSubType.java", "IndirectlyAnnotatedSubType.java"),
                    relativeGeneratedNames = listOf("IndirectlyAnnotatedSubType_GsonTypeAdapter.java")
            ), "-Agsonpath.addtionalAnnotations=generator.gson_sub_type.indirectly_annotated.IndirectSubType")

    @Test
    fun givenDuplicateKeys_whenProcessorExecuted_expectDuplicateKeysError() =
            assertPolymorphismFailure("TypesList_DuplicateKeys.java",
                    "The json key '\"type1\"' appears more than once")

    @Test
    fun givenNoKeys_whenProcessorExecuted_expectNoKeysError() =
            assertPolymorphismFailure("TypesList_NoKeys.java",
                    "Gson Path: At least one json key must be defined for GsonSubType")

    @Test
    fun givenBlankFieldName_whenProcessorExecuted_expectBlankFieldNameError() =
            assertPolymorphismFailure("TypesList_BlankFieldName.java",
                    "Gson Path: A blank json key is not valid for GsonSubType")

    @Test
    fun givenKeysAndParameterMismatch_whenProcessorExecuted_expectMismatchError() =
            assertPolymorphismFailure("TypesList_KeysAndParameterMismatch.java",
                    "Gson Path: The parameters size does not match the json keys size")

    @Test
    fun givenIncorrectReturnType_whenProcessorExecuted_expectIncorrectReturnTypeError() =
            assertPolymorphismFailure("TypesList_IncorrectReturnType.java",
                    "Gson Path: Incorrect return type for @GsonSubtypeGetter method. It must be Class<? extends TypesList_IncorrectReturnType>")

    @Test
    fun givenNoGsonSubtypeGetter_whenProcessorExecuted_expectNoGsonSubtypeGetterError() =
            assertPolymorphismFailure("TypesList_NoGsonSubtypeGetter.java",
                    "An @GsonSubtypeGetter method must be defined. See the annotation for more information")

    @Test
    fun givenTooManyGsonSubtypeGetters_whenProcessorExecuted_expectTooManyGsonSubtypeGettersError() =
            assertPolymorphismFailure("TypesList_TooManyGsonSubtypeGetters.java",
                    "Only one @GsonSubtypeGetter method may exist")

    private fun test(folder: String) =
            assertGeneratedContent(TestCriteria("generator/gson_sub_type/$folder",
                    absoluteSourceNames = listOf("generator/standard/TestGsonTypeFactory.java"),
                    relativeSourceNames = listOf("TypeGsonSubType.java"),
                    relativeGeneratedNames = listOf("TypeGsonSubType_GsonTypeAdapter.java")
            ))

    private fun assertPolymorphismFailure(className: String, errorMessage: String, folder: String = "failures") {
        Truth.assertAbout(JavaSourcesSubjectFactory.javaSources())
                .that(listOf(
                        JavaFileObjects.forResource("generator/standard/TestGsonTypeFactory.java"),
                        JavaFileObjects.forResource("generator/gson_sub_type/$folder/$className")
                ))
                .processedWith(GsonPathAdapterProcessor())
                .failsToCompile()
                .withErrorContaining(errorMessage)
    }
}
