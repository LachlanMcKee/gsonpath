package gsonpath.generator.adapter

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import gsonpath.FlattenJson
import gsonpath.generator.processingExceptionMatcher
import gsonpath.model.FieldInfo
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito
import javax.lang.model.element.Element

class AdapterAnnotationValidatorTest {

    @Rule
    @JvmField
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun givenInvalidFieldType_whenTestValidateFieldAnnotations_expectException() {
        createFieldInfo(typeName = TypeName.CHAR).apply {
            Mockito.`when`(element).thenReturn(Mockito.mock(Element::class.java))

            exception.expect(CoreMatchers.`is`(processingExceptionMatcher(element, "FlattenObject can only be used on String variables")))
            AdapterAnnotationValidator.validateFieldAnnotations(this)
        }
    }

    @Test
    fun givenValidFlattenJsonFieldType_whenTestValidateFieldAnnotations_expectNoException() {
        AdapterAnnotationValidator.validateFieldAnnotations(createFieldInfo())
    }

    @Test
    fun givenNoFlattenJsonFieldType_whenTestValidateFieldAnnotations_expectNoException() {
        AdapterAnnotationValidator.validateFieldAnnotations(createFieldInfo(hasFlattenJson = false))
    }

    private fun createFieldInfo(
            hasFlattenJson: Boolean = true,
            typeName: TypeName = ClassName.get(String::class.java)): FieldInfo {

        return Mockito.mock(FieldInfo::class.java).apply {
            if (hasFlattenJson) {
                Mockito.`when`(getAnnotation(FlattenJson::class.java)).thenReturn(Mockito.mock(FlattenJson::class.java))
            }
            Mockito.`when`(this.typeName).thenReturn(typeName)
        }
    }
}