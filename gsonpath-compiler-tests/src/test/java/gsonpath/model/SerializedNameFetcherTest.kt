package gsonpath.model

import com.google.gson.annotations.SerializedName
import gsonpath.NestedJson
import gsonpath.generator.processingExceptionMatcher
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito.mock
import javax.lang.model.element.Element
import org.mockito.Mockito.`when` as whenever

class SerializedNameFetcherTest {
    @JvmField
    @Rule
    val expectedException: ExpectedException = ExpectedException.none()

    val fieldInfo = mock(FieldInfo::class.java)
    val serializedName = mock(SerializedName::class.java)
    val nestedJson = mock(NestedJson::class.java)
    val element = mock(Element::class.java)

    @Before
    fun setup() {
        whenever(fieldInfo.element).thenReturn(element)
        whenever(serializedName.alternate).thenReturn(emptyArray())
    }

    @Test
    fun testBothSerializedAndNestedMissing_returnNull() {
        Assert.assertNull(SerializedNameFetcher.getSerializedName(fieldInfo, '.'))
    }

    @Test
    fun testBothSerializedAndNestedFound() {
        whenever(nestedJson.value).thenReturn("foo")
        whenever(fieldInfo.getAnnotation(NestedJson::class.java)).thenReturn(nestedJson)

        whenever(serializedName.value).thenReturn("bar")
        whenever(fieldInfo.getAnnotation(SerializedName::class.java)).thenReturn(serializedName)

        Assert.assertEquals("foo.bar", SerializedNameFetcher.getSerializedName(fieldInfo, '.'))
    }

    @Test
    fun testOnlyNestedFound() {
        whenever(nestedJson.value).thenReturn("foo")
        whenever(fieldInfo.getAnnotation(NestedJson::class.java)).thenReturn(nestedJson)

        Assert.assertEquals("foo.", SerializedNameFetcher.getSerializedName(fieldInfo, '.'))
    }

    @Test
    fun testOnlySerializedFound() {
        whenever(serializedName.value).thenReturn("bar")
        whenever(fieldInfo.getAnnotation(SerializedName::class.java)).thenReturn(serializedName)

        Assert.assertEquals("bar", SerializedNameFetcher.getSerializedName(fieldInfo, '.'))
    }

    @Test
    fun testNestedGsonWithTrailingDelimiter() {
        whenever(nestedJson.value).thenReturn(".")
        whenever(fieldInfo.getAnnotation(NestedJson::class.java)).thenReturn(nestedJson)

        expectedException.expect(`is`(processingExceptionMatcher(element,
                "NestedJson path must not end with '.'")))

        SerializedNameFetcher.getSerializedName(fieldInfo, '.')
    }

    @Test
    fun testSerializedNameWithAlternateProperty() {
        whenever(serializedName.alternate).thenReturn(arrayOf(""))
        whenever(fieldInfo.getAnnotation(SerializedName::class.java)).thenReturn(serializedName)

        expectedException.expect(`is`(processingExceptionMatcher(element,
                "SerializedName 'alternate' feature is not supported")))

        SerializedNameFetcher.getSerializedName(fieldInfo, '.')
    }
}