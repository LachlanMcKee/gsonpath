package gsonpath.adapter.common

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import gsonpath.GsonSubtype
import gsonpath.ProcessingException
import gsonpath.util.MethodElementContent
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import javax.lang.model.element.TypeElement

class SubTypeMetadataFactoryImplTest {
    @JvmField
    @Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    private val gsonSubTypeGetterMapper = mock<GsonSubTypeGetterMapper>()
    private val gsonSubTypeFieldInfoMapper = mock<GsonSubTypeFieldInfoMapper>()
    private val subTypeJsonKeysValidator = mock<SubTypeJsonKeysValidator>()
    private val subTypeGetterValidator = mock<SubTypeGetterValidator>()
    private val factory: SubTypeMetadataFactory = SubTypeMetadataFactoryImpl(
            gsonSubTypeGetterMapper,
            gsonSubTypeFieldInfoMapper,
            subTypeJsonKeysValidator,
            subTypeGetterValidator)

    private val expectedException = ProcessingException("FooBar")

    @Test
    fun givenJsonKeyValidationFails_thenExpectException() {
        val jsonKeys = arrayOf("")
        val element = mock<TypeElement>()

        whenever(subTypeJsonKeysValidator.validateJsonKeys(element, jsonKeys))
                .thenReturn(expectedException)

        exceptionRule.expect(`is`(expectedException))

        factory.getGsonSubType(createGsonSubtype(jsonKeys), element)
    }

    @Test
    fun givenGsonSubtypeGetterValidationFails_thenExpectException() {
        val element = mock<TypeElement>()
        val getterMethodElementContent = mock<MethodElementContent>()

        whenever(gsonSubTypeGetterMapper.mapElementToGetterMethod(element))
                .thenReturn(getterMethodElementContent)

        whenever(subTypeGetterValidator.validateGsonSubtypeGetterMethod(element, getterMethodElementContent))
                .thenReturn(expectedException)

        exceptionRule.expect(`is`(expectedException))

        factory.getGsonSubType(createGsonSubtype(arrayOf("abc")), element)
    }

    @Test
    fun givenValidGetter_thenExpectSubTypeMetadata() {
        val jsonKeys = arrayOf("abc")
        val element = mock<TypeElement>()
        val getterMethodElementContent = mock<MethodElementContent> {
            whenever(it.methodName).thenReturn("methodName")
        }

        whenever(gsonSubTypeGetterMapper.mapElementToGetterMethod(element))
                .thenReturn(getterMethodElementContent)

        val fieldInfoList = listOf(mock<GsonSubTypeFieldInfo>())
        whenever(gsonSubTypeFieldInfoMapper.mapToFieldInfo(getterMethodElementContent, jsonKeys))
                .thenReturn(fieldInfoList)

        val actualSubType = factory.getGsonSubType(createGsonSubtype(jsonKeys), element)
        assertEquals(SubTypeMetadata(fieldInfoList, "methodName"), actualSubType)
    }

    private fun createGsonSubtype(jsonKeys: Array<String>) = mock<GsonSubtype> {
        whenever(it.jsonKeys).thenReturn(jsonKeys)
    }
}