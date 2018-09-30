package gsonpath.model

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import gsonpath.GsonFieldValidationType
import gsonpath.PathSubstitution
import gsonpath.ProcessingException
import org.junit.Assert
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(Enclosed::class)
class GsonObjectFactoryTest {

    class StandardTests : BaseGsonObjectFactoryTest() {
        @Test
        @Throws(ProcessingException::class)
        fun givenNoJsonPathAnnotation_whenAddGsonType_expectSingleGsonObject() {
            // when
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)

            // when
            val outputGsonObject = executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo))

            // then
            val expectedGsonObject = GsonObject()
            expectedGsonObject.addField(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, GsonField(0, fieldInfo, BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, false, null))
            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenJsonPath_whenAddGsonType_expectMultipleGsonObjects() {
            // when
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, "root.child")

            // when
            val outputGsonObject = executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo))

            // then
            val expectedGsonObject = GsonObject()
            val gsonObject = GsonObject()
            gsonObject.addField("child", GsonField(0, fieldInfo, "root.child", false, null))
            expectedGsonObject.addObject("root", gsonObject)

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenJsonPathWithDanglingDelimiter_whenAddGsonType_expectMultipleGsonObjects() {
            // when
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, "root.")

            // when
            val outputGsonObject = executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo))

            // then
            val expectedGsonObject = GsonObject()
            val gsonObject = GsonObject()
            gsonObject.addField(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, GsonField(0, fieldInfo, "root." + BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, false, null))
            expectedGsonObject.addObject("root", gsonObject)

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenPathSubstitution_whenAddGsonType_expectReplacedJsonPath() {
            // given
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, "{REPLACE_ME_1}.{REPLACE_ME_2}")
            val pathSubstitution1 = mock(PathSubstitution::class.java)
            `when`<String>(pathSubstitution1.original).thenReturn("REPLACE_ME_1")
            `when`<String>(pathSubstitution1.replacement).thenReturn("replacement")

            val pathSubstitution2 = mock(PathSubstitution::class.java)
            `when`<String>(pathSubstitution2.original).thenReturn("REPLACE_ME_2")
            `when`<String>(pathSubstitution2.replacement).thenReturn("value")

            // when
            val outputGsonObject = executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo, pathSubstitutions = arrayOf(pathSubstitution1, pathSubstitution2)))

            // then
            val expectedGsonObject = GsonObject()
            expectedGsonObject.addObject("replacement", GsonObject())
                    .addField("value", GsonField(0, fieldInfo, "replacement.value", false, null))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenObjectType_whenAddGsonType_throwInvalidFieldTypeException() {
            // given
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)
            `when`(fieldInfo.typeName).thenReturn(TypeName.OBJECT)

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Invalid field type: java.lang.Object")
            executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo))
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenBothNonNullAndNullableAnnotations_whenAddGsonType_throwIncorrectAnnotationsException() {
            // given
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)
            `when`(fieldInfo.typeName).thenReturn(TypeName.INT.box())
            `when`(fieldInfo.annotationNames).thenReturn(listOf("NonNull", "Nullable"))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Field cannot have both Mandatory and Optional annotations")
            executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo))
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenDuplicateChildFields_whenAddGsonType_throwDuplicateFieldException() {
            // given
            val existingGsonObject = GsonObject()
            val existingField = GsonField(0, mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME), BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, false, null)
            existingGsonObject.addField(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, existingField)

            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Unexpected duplicate field 'variableName' found. Each tree branch must use a unique value!")
            executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo), existingGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenExistingObjectField_whenAddGsonType_throwDuplicateFieldException() {
            // given
            val duplicateBranchName = "duplicate"

            val existingGsonObject = GsonObject()
            val existingField = GsonField(0, mockFieldInfo(duplicateBranchName), duplicateBranchName, false, null)
            existingGsonObject.addField(duplicateBranchName, existingField)

            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, duplicateBranchName + "." + BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Unexpected duplicate field 'duplicate' found. Each tree branch must use a unique value!")
            executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo), existingGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenExistingObjectField_whenAddNestedField_throwDuplicateFieldException() {
            // given
            val duplicateBranchName = "duplicate"

            val existingGsonObject = GsonObject()
            val existingField = GsonField(0, mockFieldInfo(duplicateBranchName), duplicateBranchName, false, null)

            val childObject = GsonObject()
            childObject.addField(duplicateBranchName, existingField)
            existingGsonObject.addObject(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, childObject)

            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME + "." + duplicateBranchName)

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Unexpected duplicate field 'duplicate' found. Each tree branch must use a unique value!")
            executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo), existingGsonObject)
        }
    }

    @RunWith(Parameterized::class)
    class RequiredAnnotationsTest(
            private val requiredTypeAnnotation: String?,
            private val gsonFieldValidationType: GsonFieldValidationType,
            private val fieldTypeName: TypeName,
            private val isRequired: Boolean) : BaseGsonObjectFactoryTest() {

        @Test
        @Throws(ProcessingException::class)
        fun test() {
            // when
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)
            `when`(fieldInfo.typeName).thenReturn(fieldTypeName)

            if (requiredTypeAnnotation != null) {
                `when`(fieldInfo.annotationNames).thenReturn(listOf(requiredTypeAnnotation))
            }

            // when
            val outputGsonObject = executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo, gsonFieldValidationType = gsonFieldValidationType))

            // then
            val expectedGsonObject = GsonObject()
            expectedGsonObject.addField(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, GsonField(0, fieldInfo, BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, isRequired, null))
            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<Any?>> {
                return listOf(
                        // Test 'NonNull' annotation permutations with a non-primitive type
                        arrayOf("NonNull", GsonFieldValidationType.NO_VALIDATION, TypeName.INT.box(), false),
                        arrayOf("NonNull", GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, TypeName.INT.box(), true),
                        arrayOf("NonNull", GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, TypeName.INT.box(), true),

                        // Test 'Nullable' annotation permutations with a non-primitive type
                        arrayOf("Nullable", GsonFieldValidationType.NO_VALIDATION, TypeName.INT.box(), false),
                        arrayOf("Nullable", GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, TypeName.INT.box(), false),
                        arrayOf("Nullable", GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, TypeName.INT.box(), false),

                        // Test no annotation permutations with a non-primitive type
                        arrayOf(null, GsonFieldValidationType.NO_VALIDATION, TypeName.INT.box(), false),
                        arrayOf(null, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, TypeName.INT.box(), true),
                        arrayOf(null, GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, TypeName.INT.box(), false),

                        // Test no annotation permutations with a primitive type
                        arrayOf(null, GsonFieldValidationType.NO_VALIDATION, TypeName.INT, false),
                        arrayOf(null, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, TypeName.INT, true),
                        arrayOf(null, GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, TypeName.INT, true)
                )
            }
        }
    }

    @RunWith(Parameterized::class)
    class MandatoryAnnotationsTest(private val mandatoryAnnotation: String) : BaseGsonObjectFactoryTest() {

        @Test
        @Throws(ProcessingException::class)
        fun givenPrimitiveField_whenAddGsonType_throwProcessingException() {
            // when
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)
            `when`(fieldInfo.annotationNames).thenReturn(listOf(mandatoryAnnotation))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Primitives should not use NonNull or Nullable annotations")
            executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo))
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenNonPrimitiveFieldAndValidateNonNull_whenAddGsonType_expectIsRequired() {
            // when
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)
            `when`(fieldInfo.typeName).thenReturn(ClassName.INT.box())
            `when`(fieldInfo.annotationNames).thenReturn(listOf(mandatoryAnnotation))

            // when
            val outputGsonObject = executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo, gsonFieldValidationType = GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL))

            // then
            val expectedGsonObject = GsonObject()
            expectedGsonObject.addField(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, GsonField(0, fieldInfo, BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, true, null))
            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<Any>> {
                return listOf(
                        arrayOf<Any>("NonNull"),
                        arrayOf<Any>("Nonnull"),
                        arrayOf<Any>("NotNull"),
                        arrayOf<Any>("Notnull")
                )
            }
        }
    }
}
