package gsonpath.adapter.standard.extension.def.stringdef

import gsonpath.TestUtil
import org.junit.Assert
import org.junit.Test

class StringDefTests {

    private fun getValue(jsonString: String): StringDefModel {
        return TestUtil.executeFromJson(StringDefModel::class.java, jsonString)
    }

    @Test
    fun givenValidValue_whenJsonParsed_thenFieldEqualsAnnotationStringReference() {
        val model = getValue("{\"value\": \"1\"}")

        // Note: We are deliberating checking the String reference as opposed to using .equals
        Assert.assertTrue(model.value === StringDefExample.VALUE_1)
    }

    @Test
    fun givenNoValue_whenJsonParsed_thenFieldIsNull() {
        val model = getValue("{}")
        Assert.assertNull(model.value)
    }

    @Test
    fun givenInvalidValue_whenJsonParsed_thenThrowsException() {
        TestUtil.expectException(StringDefModel::class.java, "{\"value\": \"3\"}", "Unexpected String '3' for JSON element 'value'")
    }
}

