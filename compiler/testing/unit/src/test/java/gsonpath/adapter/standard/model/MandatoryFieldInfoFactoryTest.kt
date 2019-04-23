package gsonpath.adapter.standard.model

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Test

class MandatoryFieldInfoFactoryTest {

    @Test
    fun givenNoRequiredFields_thenExpectEmptyList() {
        test(GsonObject(fieldMap = mapOf("foo" to createField(false))), emptyList())
    }

    @Test
    fun givenRequiredFields_thenExpectFields() {
        val field1 = createField(true)
        val field2 = createField(true)
        val field3 = createField(true)
        val field4 = createField(true)

        val nestedObject = GsonObject(fieldMap = mapOf("bar" to field2))
        val array = GsonArray(arrayFields = mapOf(1 to GsonObject(fieldMap = mapOf("bar" to field4)), 0 to field3), maxIndex = 2)
        val rootObject = GsonObject(fieldMap = mapOf(
                "value2" to nestedObject,
                "value3" to array,
                "value1" to field1
        ))
        test(rootObject, listOf(field2, field4, field3, field1))
    }

    private fun createField(isRequired: Boolean = false): GsonField {
        return mock<GsonField>().apply { whenever(this.isRequired).thenReturn(isRequired) }
    }

    private fun test(rootObject: GsonObject, expectedList: List<GsonField>) {
        Assert.assertEquals(
                expectedList,
                MandatoryFieldInfoFactory().createMandatoryFieldsFromGsonObject(rootObject))
    }
}
