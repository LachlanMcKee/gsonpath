package gsonpath.unit.adapter.standard.model

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import gsonpath.adapter.standard.model.GsonArray
import gsonpath.adapter.standard.model.GsonField
import gsonpath.adapter.standard.model.GsonObject
import gsonpath.adapter.standard.model.MandatoryFieldInfoFactory
import gsonpath.simpleGsonObject
import org.junit.Assert
import org.junit.Test

class MandatoryFieldInfoFactoryTest {

    @Test
    fun givenNoRequiredFields_thenExpectEmptyList() {
        test(simpleGsonObject("foo", createField(false)), emptyList())
    }

    @Test
    fun givenRequiredFields_thenExpectFields() {
        val field1 = createField(true)
        val field2 = createField(true)
        val field3 = createField(true)
        val field4 = createField(true)

        val nestedObject = simpleGsonObject("bar", field2)
        val array = GsonArray(arrayFields = mapOf(1 to simpleGsonObject("bar", field4), 0 to field3), maxIndex = 2)
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
