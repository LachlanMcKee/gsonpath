package gsonpath.unit.adapter.standard.model

import com.nhaarman.mockitokotlin2.mock
import com.squareup.javapoet.TypeName
import gsonpath.ProcessingException
import gsonpath.adapter.standard.model.GsonObjectValidator
import gsonpath.model.FieldType
import gsonpath.unit.model.FieldInfoTestFactory.mockFieldInfo
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.`when` as whenever

@RunWith(Enclosed::class)
class GsonObjectValidatorTest {

    class ExceptionTests {
        @JvmField
        @Rule
        val exception: ExpectedException = ExpectedException.none()

        @Test
        @Throws(ProcessingException::class)
        fun givenBothNonNullAndNullableAnnotations_whenValidate_throwIncorrectAnnotationsException() {
            // given
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)
            whenever(fieldInfo.fieldType).thenReturn(FieldType.Other(TypeName.INT.box(), mock()))
            whenever(fieldInfo.annotationNames).thenReturn(listOf("NonNull", "Nullable"))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Field cannot have both Mandatory and Optional annotations")
            GsonObjectValidator().validate(fieldInfo)
        }

        @Test
        @Throws(ProcessingException::class)
        fun givenObjectType_whenValidate_throwInvalidFieldTypeException() {
            // given
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)
            whenever(fieldInfo.fieldType).thenReturn(FieldType.Other(TypeName.OBJECT, mock()))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Invalid field type: java.lang.Object")
            GsonObjectValidator().validate(fieldInfo)
        }
    }

    @RunWith(Parameterized::class)
    class MandatoryAnnotationsTest(private val mandatoryAnnotation: String) {
        @JvmField
        @Rule
        val exception: ExpectedException = ExpectedException.none()

        @Test
        @Throws(ProcessingException::class)
        fun givenPrimitiveField_whenAddGsonType_throwProcessingException() {
            // when
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)
            whenever(fieldInfo.annotationNames).thenReturn(listOf(mandatoryAnnotation))

            // when / then
            exception.expect(ProcessingException::class.java)
            exception.expectMessage("Primitives should not use NonNull or Nullable annotations")
            GsonObjectValidator().validate(fieldInfo)
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<Any>> = listOf(
                    arrayOf<Any>("NonNull"),
                    arrayOf<Any>("Nonnull"),
                    arrayOf<Any>("NotNull"),
                    arrayOf<Any>("Notnull")
            )
        }
    }

    @RunWith(Parameterized::class)
    class ValidResultTests(
            private val requiredTypeAnnotation: String?,
            private val expectedResult: GsonObjectValidator.Result) {

        @Test
        @Throws(ProcessingException::class)
        fun test() {
            // when
            val fieldInfo = mockFieldInfo(BaseGsonObjectFactoryTest.DEFAULT_VARIABLE_NAME)
            whenever(fieldInfo.fieldType).thenReturn(FieldType.Other(TypeName.INT.box(), mock()))
            if (requiredTypeAnnotation != null) {
                whenever(fieldInfo.annotationNames).thenReturn(listOf(requiredTypeAnnotation))
            }

            // when
            val result = GsonObjectValidator().validate(fieldInfo)

            // then
            Assert.assertEquals(expectedResult, result)
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<out Any?>> = listOf(
                    arrayOf("NonNull", GsonObjectValidator.Result.Mandatory),
                    arrayOf("Nonnull", GsonObjectValidator.Result.Mandatory),
                    arrayOf("NotNull", GsonObjectValidator.Result.Mandatory),
                    arrayOf("Notnull", GsonObjectValidator.Result.Mandatory),
                    arrayOf("Nullable", GsonObjectValidator.Result.Optional),
                    arrayOf("Unknown", GsonObjectValidator.Result.Standard),
                    arrayOf(null, GsonObjectValidator.Result.Standard)
            )
        }
    }
}