package gsonpath.adapter.enums

import com.google.gson.FieldNamingPolicy
import com.google.gson.annotations.SerializedName
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.squareup.javapoet.ClassName
import gsonpath.annotation.EnumGsonAdapter
import gsonpath.processingExceptionMatcher
import gsonpath.util.AnnotationFetcher
import gsonpath.util.FieldElementContent
import gsonpath.util.TypeHandler
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import javax.lang.model.element.Element
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement

class EnumAdapterPropertiesFactoryTest {
    @JvmField
    @Rule
    val expectedException: ExpectedException = ExpectedException.none()

    private val typeHandler: TypeHandler = mock()
    private val annotationFetcher: AnnotationFetcher = mock()
    private val enumFieldLabelMapper: EnumFieldLabelMapper = mock()

    private val factory: EnumAdapterPropertiesFactory = EnumAdapterPropertiesFactory(
            typeHandler, annotationFetcher, enumFieldLabelMapper
    )

    private val enumElement = mock<TypeElement> {
        whenever(it.toString()).thenReturn("foo.bar.EnumTest")
    }
    private val enumClassName = mock<ClassName>()

    private val enumValueName1 = "EnumValue1"
    private val enumValueName2 = "EnumValue2"

    private val enumValueElement1 = createEnumElement(enumValueName1)
    private val enumValueElement2 = createEnumElement(enumValueName2)

    private val enumField1 = FieldElementContent(enumValueElement1, mock())
    private val enumField2 = FieldElementContent(enumValueElement2, mock())

    private fun createEnumElement(name: String): Element {
        val enumValue1Name = mock<Name> {
            whenever(it.toString()).thenReturn(name)
        }
        return mock {
            whenever(it.simpleName).thenReturn(enumValue1Name)
        }
    }

    @Before
    fun setup() {
        whenever(typeHandler.getClassName(enumElement)).thenReturn(enumClassName)
    }

    @Test
    fun givenNoFields_whenCreate_thenExpectEmptyModel() {
        assertEquals(
                EnumAdapterProperties(
                        enumTypeName = enumClassName,
                        fields = emptyList(),
                        defaultValue = null
                ),
                factory.create(enumElement, FieldNamingPolicy.IDENTITY)
        )
    }

    @Test
    fun givenOneFieldWithNoDefault_whenCreate_thenExpectSingleFieldWithNoDefaultModel() {
        whenever(typeHandler.getFields(eq(enumElement), any()))
                .thenReturn(listOf(enumField1))

        val enumValue1ClassName = setElementLabel(enumValueName1)

        assertEquals(
                EnumAdapterProperties(
                        enumTypeName = enumClassName,
                        fields = listOf(EnumAdapterProperties.EnumField(enumValue1ClassName, "EnumValue1")),
                        defaultValue = null
                ),
                factory.create(enumElement, FieldNamingPolicy.IDENTITY)
        )
    }

    @Test
    fun givenTwoFieldsWithOneDefault_whenCreate_thenExpectTwoFieldsWithDefaultModel() {
        whenever(typeHandler.getFields(eq(enumElement), any()))
                .thenReturn(listOf(enumField1, enumField2))

        val enumValue1ClassName = setElementLabel(enumValueName1)
        val enumValue2ClassName = setElementLabel(enumValueName2)
        val enumField2 = EnumAdapterProperties.EnumField(enumValue2ClassName, "EnumValue2")

        setDefaultAnnotationOnElement(enumValueElement2)

        assertEquals(
                EnumAdapterProperties(
                        enumTypeName = enumClassName,
                        fields = listOf(
                                EnumAdapterProperties.EnumField(enumValue1ClassName, "EnumValue1"),
                                enumField2
                        ),
                        defaultValue = enumField2
                ),
                factory.create(enumElement, FieldNamingPolicy.IDENTITY)
        )
    }

    @Test
    fun givenTwoFieldsWithTwoDefaults_whenCreate_thenExpectException() {
        whenever(typeHandler.getFields(eq(enumElement), any()))
                .thenReturn(listOf(enumField1, enumField2))

        setElementLabel(enumValueName1)
        setElementLabel(enumValueName2)

        setDefaultAnnotationOnElement(enumValueElement1)
        setDefaultAnnotationOnElement(enumValueElement2)

        expectedException.expect(`is`(processingExceptionMatcher(enumElement,
                "Only one DefaultValue can be defined")))

        factory.create(enumElement, FieldNamingPolicy.IDENTITY)
    }

    @Test
    fun givenOneFieldsWithSerializedName_whenCreate_thenExpectSingleFieldDifferentName() {
        whenever(typeHandler.getFields(eq(enumElement), any()))
                .thenReturn(listOf(enumField1))

        val enumValue1ClassName = setElementLabel(enumValueName1)

        val serializedName = mock<SerializedName> {
            whenever(it.value).thenReturn("CustomLabel")
        }
        whenever(annotationFetcher.getAnnotation(enumElement, enumValueElement1, SerializedName::class.java))
                .thenReturn(serializedName)

        assertEquals(
                EnumAdapterProperties(
                        enumTypeName = enumClassName,
                        fields = listOf(EnumAdapterProperties.EnumField(enumValue1ClassName, "CustomLabel")),
                        defaultValue = null
                ),
                factory.create(enumElement, FieldNamingPolicy.IDENTITY)
        )
    }

    private fun setElementLabel(name: String): ClassName {
        val enumValueClassName = mock<ClassName>()
        whenever(typeHandler.guessClassName("foo.bar.EnumTest.$name")).thenReturn(enumValueClassName)

        whenever(enumFieldLabelMapper.map(name, FieldNamingPolicy.IDENTITY))
                .thenReturn(name)

        return enumValueClassName
    }

    private fun setDefaultAnnotationOnElement(valueElement: Element) {
        whenever(annotationFetcher.getAnnotation(enumElement, valueElement, EnumGsonAdapter.DefaultValue::class.java))
                .thenReturn(mock())
    }
}