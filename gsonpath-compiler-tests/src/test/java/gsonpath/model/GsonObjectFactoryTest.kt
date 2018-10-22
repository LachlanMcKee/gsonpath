package gsonpath.model

import com.squareup.javapoet.TypeName
import gsonpath.GsonFieldValidationType
import gsonpath.ProcessingException
import gsonpath.model.FieldInfoTestFactory.mockFieldInfo
import gsonpath.model.GsonObjectValidator.Result.*
import org.junit.Assert
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.`when` as whenever

@RunWith(Enclosed::class)
class GsonObjectFactoryTest {

    class StandardTests : BaseGsonObjectFactoryTest() {
        @Test
        @Throws(ProcessingException::class)
        fun givenNoJsonPathAnnotation_whenAddGsonType_expectSingleGsonObject() {
            // when
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)
            val metadata = createMetadata()

            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Standard(DEFAULT_VARIABLE_NAME))

            // when
            val outputGsonObject = executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo), metadata)

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
            val metadata = createMetadata()

            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("root.child"))

            // when
            val outputGsonObject = executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo), metadata)

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
            val metadata = createMetadata()

            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("root.variableName"))

            // when
            val outputGsonObject = executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo), metadata)

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

            val metadata = createMetadata()
            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("replacement.value"))

            // when
            val outputGsonObject = executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = GsonObject()
            expectedGsonObject.addObject("replacement", GsonObject())
                    .addField("value", GsonField(0, fieldInfo, "replacement.value", false, null))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenDuplicateChildFields_whenAddGsonType_throwDuplicateFieldException() {
            // given
            val existingGsonObject = GsonObject()
            val existingField = GsonField(0, mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME), BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, false, null)
            existingGsonObject.addField(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME, existingField)

            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)

            val metadata = createMetadata()
            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Standard(DEFAULT_VARIABLE_NAME))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Unexpected duplicate field 'variableName' found. Each tree branch must use a unique value!")
            executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo), metadata, existingGsonObject)
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

            val metadata = createMetadata()
            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested(duplicateBranchName + "." + BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Unexpected duplicate field 'duplicate' found. Each tree branch must use a unique value!")
            executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo), metadata, existingGsonObject)
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

            val metadata = createMetadata()
            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME + "." + duplicateBranchName))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Unexpected duplicate field 'duplicate' found. Each tree branch must use a unique value!")
            executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo), metadata, existingGsonObject)
        }
    }

    @RunWith(Parameterized::class)
    class RequiredAnnotationsTest(
            private val validationResult: GsonObjectValidator.Result,
            private val gsonFieldValidationType: GsonFieldValidationType,
            private val fieldTypeName: TypeName,
            private val isRequired: Boolean) : BaseGsonObjectFactoryTest() {

        @Test
        @Throws(ProcessingException::class)
        fun test() {
            // when
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)
            whenever(fieldInfo.typeName).thenReturn(fieldTypeName)

            whenever(gsonObjectValidator.validate(fieldInfo)).thenReturn(validationResult)

            val metadata = createMetadata(gsonFieldValidationType = gsonFieldValidationType)
            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Standard(DEFAULT_VARIABLE_NAME))

            // when
            val outputGsonObject = executeAddGsonType(BaseGsonObjectFactoryTest.GsonTypeArguments(fieldInfo), metadata)

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
                        arrayOf(Mandatory, GsonFieldValidationType.NO_VALIDATION, TypeName.INT.box(), false),
                        arrayOf(Mandatory, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, TypeName.INT.box(), true),
                        arrayOf(Mandatory, GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, TypeName.INT.box(), true),

                        // Test 'Nullable' annotation permutations with a non-primitive type
                        arrayOf(Optional, GsonFieldValidationType.NO_VALIDATION, TypeName.INT.box(), false),
                        arrayOf(Optional, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, TypeName.INT.box(), false),
                        arrayOf(Optional, GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, TypeName.INT.box(), false),

                        // Test no annotation permutations with a non-primitive type
                        arrayOf(Standard, GsonFieldValidationType.NO_VALIDATION, TypeName.INT.box(), false),
                        arrayOf(Standard, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, TypeName.INT.box(), true),
                        arrayOf(Standard, GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, TypeName.INT.box(), false),

                        // Test no annotation permutations with a primitive type
                        arrayOf(Standard, GsonFieldValidationType.NO_VALIDATION, TypeName.INT, false),
                        arrayOf(Standard, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE, TypeName.INT, true),
                        arrayOf(Standard, GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL, TypeName.INT, true)
                )
            }
        }
    }
}
