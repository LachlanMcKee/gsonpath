package gsonpath.unit.adapter.standard.model

import com.nhaarman.mockitokotlin2.mock
import com.squareup.javapoet.TypeName
import gsonpath.GsonFieldValidationType
import gsonpath.GsonFieldValidationType.*
import gsonpath.ProcessingException
import gsonpath.adapter.standard.model.FieldPath
import gsonpath.adapter.standard.model.GsonObjectValidator
import gsonpath.adapter.standard.model.GsonObjectValidator.Result.*
import gsonpath.adapter.standard.model.MutableGsonField
import gsonpath.adapter.standard.model.MutableGsonObject
import gsonpath.model.FieldType
import gsonpath.unit.model.FieldInfoTestFactory.mockFieldInfo
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
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME)
            val metadata = createMetadata()

            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Standard(DEFAULT_VARIABLE_NAME))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject()
            expectedGsonObject.addField(DEFAULT_VARIABLE_NAME, MutableGsonField(0, fieldInfo, DEFAULT_VARIABLE_NAME_2, DEFAULT_VARIABLE_NAME, false))
            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenJsonPath_whenAddGsonType_expectMultipleGsonObjects() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "root.child")
            val metadata = createMetadata()

            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("root.child"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject()
            val mutableGsonObject = MutableGsonObject()
            mutableGsonObject.addField("child", MutableGsonField(0, fieldInfo, "value_root_child", "root.child", false))
            expectedGsonObject.addObject("root", mutableGsonObject)

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenJsonPathWithDanglingDelimiter_whenAddGsonType_expectMultipleGsonObjects() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "root.")
            val metadata = createMetadata()

            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("root.variableName"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject()
            val mutableGsonObject = MutableGsonObject()
            mutableGsonObject.addField(DEFAULT_VARIABLE_NAME, MutableGsonField(0, fieldInfo, "value_root_$DEFAULT_VARIABLE_NAME", "root.$DEFAULT_VARIABLE_NAME", false))
            expectedGsonObject.addObject("root", mutableGsonObject)

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenPathSubstitution_whenAddGsonType_expectReplacedJsonPath() {
            // given
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "{REPLACE_ME_1}.{REPLACE_ME_2}")

            val metadata = createMetadata()
            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("replacement.value"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject()
            expectedGsonObject.addObject("replacement", MutableGsonObject())
                    .addField("value", MutableGsonField(0, fieldInfo, "value_replacement_value", "replacement.value", false))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenJsonPathArray_whenAddGsonType_expectRootArray() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "element[5]")
            val metadata = createMetadata()

            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Standard("element[5]"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject()
            val elementArray = expectedGsonObject.addArray("element")
            elementArray.addField(5, MutableGsonField(0, fieldInfo, "value_element_5_", "element[5]", false))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenJsonPathArrayNestedWithinObject_whenAddGsonType_expectArrayNestedWithinObject() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "value.element[5]")
            val metadata = createMetadata()

            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("value.element[5]"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject()
            val valueObject = MutableGsonObject()
            expectedGsonObject.addObject("value", valueObject)
            val elementArray = valueObject.addArray("element")
            elementArray.addField(5, MutableGsonField(0, fieldInfo, "value_value_element_5_", "value.element[5]", false))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenObjectNestedWithinJsonPathArray_whenAddGsonType_expectObjectNestedWithinArray() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "element[5].value")
            val metadata = createMetadata()

            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("element[5].value"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject()
            val elementArray = expectedGsonObject.addArray("element")
            val elementItemObject = elementArray.getObjectAtIndex(5)
            elementItemObject.addField("value", MutableGsonField(0, fieldInfo, "value_element_5__value", "element[5].value", false))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenMultipleJsonPathArrays_whenAddGsonType_expectMultipleArrays() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "element[5].value[0]")
            val metadata = createMetadata()

            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("element[5].value[0]"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject()
            val elementArray = expectedGsonObject.addArray("element")
            val elementItemObject = elementArray.getObjectAtIndex(5)
            val valueArray = elementItemObject.addArray("value")
            valueArray.addField(0, MutableGsonField(0, fieldInfo, "value_element_5__value_0_", "element[5].value[0]", false))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenMultipleJsonPathArrays_whenAddGsonType_expectMultipleArrays2() {
            // when
            val metadata = createMetadata()

            val defGsonFieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "element.value[5].test.def")

            whenever(fieldPathFetcher.getJsonFieldPath(defGsonFieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("element.value[5].test.def"))

            val previousGsonObject = executeAddGsonType(GsonTypeArguments(defGsonFieldInfo), metadata)
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "element.value[5].test.abc")

            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("element.value[5].test.abc"))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata, previousGsonObject)

            // then
            val expectedGsonObject = MutableGsonObject()
            val elementObject = expectedGsonObject.addObject("element", MutableGsonObject())
            val valueArray = elementObject.addArray("value")
            val testObject = valueArray.getObjectAtIndex(5)
            val abcObject = testObject.addObject("test", MutableGsonObject())

            abcObject.addField("def", MutableGsonField(0, defGsonFieldInfo, "value_element_value_5__test_def", "element.value[5].test.def", false))
            abcObject.addField("abc", MutableGsonField(0, fieldInfo, "value_element_value_5__test_abc", "element.value[5].test.abc", false))

            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenDuplicateChildFields_whenAddGsonType_throwDuplicateFieldException() {
            // given
            val existingGsonObject = MutableGsonObject()
            val existingField = MutableGsonField(0, mockFieldInfo(DEFAULT_VARIABLE_NAME), DEFAULT_VARIABLE_NAME_2, DEFAULT_VARIABLE_NAME, false)
            existingGsonObject.addField(DEFAULT_VARIABLE_NAME, existingField)

            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME)

            val metadata = createMetadata()
            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Standard(DEFAULT_VARIABLE_NAME))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Unexpected duplicate field 'variableName' found. Each tree branch must use a unique value!")
            executeAddGsonType(GsonTypeArguments(fieldInfo), metadata, existingGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenExistingObjectField_whenAddGsonType_throwDuplicateFieldException() {
            // given
            val duplicateBranchName = "duplicate"

            val existingGsonObject = MutableGsonObject()
            val existingField = MutableGsonField(0, mockFieldInfo(duplicateBranchName), "value_$duplicateBranchName", duplicateBranchName, false)
            existingGsonObject.addField(duplicateBranchName, existingField)

            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "$duplicateBranchName.$DEFAULT_VARIABLE_NAME")

            val metadata = createMetadata()
            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("$duplicateBranchName.$DEFAULT_VARIABLE_NAME"))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Unexpected duplicate field 'duplicate' found. Each tree branch must use a unique value!")
            executeAddGsonType(GsonTypeArguments(fieldInfo), metadata, existingGsonObject)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenExistingObjectField_whenAddNestedField_throwDuplicateFieldException() {
            // given
            val duplicateBranchName = "duplicate"

            val existingGsonObject = MutableGsonObject()
            val existingField = MutableGsonField(0, mockFieldInfo(duplicateBranchName), "value_$duplicateBranchName", duplicateBranchName, false)

            val childObject = MutableGsonObject()
            childObject.addField(duplicateBranchName, existingField)
            existingGsonObject.addObject(DEFAULT_VARIABLE_NAME, childObject)

            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME, "$DEFAULT_VARIABLE_NAME.$duplicateBranchName")

            val metadata = createMetadata()
            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Nested("$DEFAULT_VARIABLE_NAME.$duplicateBranchName"))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Unexpected duplicate field 'duplicate' found. Each tree branch must use a unique value!")
            executeAddGsonType(GsonTypeArguments(fieldInfo), metadata, existingGsonObject)
        }
    }

    @RunWith(Parameterized::class)
    class RequiredAnnotationsTest(
            private val validationResult: GsonObjectValidator.Result,
            private val gsonFieldValidationType: GsonFieldValidationType,
            private val fieldType: FieldType,
            private val isRequired: Boolean) : BaseGsonObjectFactoryTest() {

        @Test
        @Throws(ProcessingException::class)
        fun test() {
            // when
            val fieldInfo = mockFieldInfo(DEFAULT_VARIABLE_NAME)
            whenever(fieldInfo.fieldType).thenReturn(fieldType)

            whenever(gsonObjectValidator.validate(fieldInfo)).thenReturn(validationResult)

            val metadata = createMetadata(gsonFieldValidationType = gsonFieldValidationType)
            whenever(fieldPathFetcher.getJsonFieldPath(fieldInfo, metadata))
                    .thenReturn(FieldPath.Standard(DEFAULT_VARIABLE_NAME))

            // when
            val outputGsonObject = executeAddGsonType(GsonTypeArguments(fieldInfo), metadata)

            // then
            val expectedGsonObject = MutableGsonObject()
            expectedGsonObject.addField(DEFAULT_VARIABLE_NAME, MutableGsonField(0, fieldInfo, DEFAULT_VARIABLE_NAME_2, DEFAULT_VARIABLE_NAME, isRequired))
            Assert.assertEquals(expectedGsonObject, outputGsonObject)
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<Any>> {
                return listOf(
                        // Test 'NonNull' annotation permutations with a non-primitive type
                        arrayOf(Mandatory, NO_VALIDATION, FieldType.Other(TypeName.INT.box(), mock()), false),
                        arrayOf(Mandatory, VALIDATE_ALL_EXCEPT_NULLABLE, FieldType.Other(TypeName.INT.box(), mock()), true),
                        arrayOf(Mandatory, VALIDATE_EXPLICIT_NON_NULL, FieldType.Other(TypeName.INT.box(), mock()), true),

                        // Test 'Nullable' annotation permutations with a non-primitive type
                        arrayOf(Optional, NO_VALIDATION, FieldType.Other(TypeName.INT.box(), mock()), false),
                        arrayOf(Optional, VALIDATE_ALL_EXCEPT_NULLABLE, FieldType.Other(TypeName.INT.box(), mock()), false),
                        arrayOf(Optional, VALIDATE_EXPLICIT_NON_NULL, FieldType.Other(TypeName.INT.box(), mock()), false),

                        // Test no annotation permutations with a non-primitive type
                        arrayOf(Standard, NO_VALIDATION, FieldType.Other(TypeName.INT.box(), mock()), false),
                        arrayOf(Standard, VALIDATE_ALL_EXCEPT_NULLABLE, FieldType.Other(TypeName.INT.box(), mock()), true),
                        arrayOf(Standard, VALIDATE_EXPLICIT_NON_NULL, FieldType.Other(TypeName.INT.box(), mock()), false),

                        // Test no annotation permutations with a primitive type
                        arrayOf(Standard, NO_VALIDATION, FieldType.Primitive(TypeName.INT, mock()), false),
                        arrayOf(Standard, VALIDATE_ALL_EXCEPT_NULLABLE, FieldType.Primitive(TypeName.INT, mock()), true),
                        arrayOf(Standard, VALIDATE_EXPLICIT_NON_NULL, FieldType.Primitive(TypeName.INT, mock()), true)
                )
            }
        }
    }
}
