package gsonpath.generator.extension.def.intdef

import gsonpath.extension.TestUtil
import org.junit.Assert
import org.junit.Test

class IntDefTests {

    private fun getValue(jsonString: String): IntDefModel {
        return TestUtil.executeFromJson(IntDefModel::class.java, jsonString)
    }

    @Test
    fun givenValidValue_whenJsonParsed_thenParsesWithoutErrors() {
        val model = getValue("{\"value\": 2}")
        Assert.assertEquals(2, model.value)
    }

    @Test
    fun givenNoValue_whenJsonParsed_thenFieldIsNull() {
        val model = getValue("{}")
        Assert.assertNull(model.value)
    }

    @Test
    fun givenInvalidValue_whenJsonParsed_thenThrowsException() {
        TestUtil.expectException(IntDefModel::class.java, "{\"value\": 3}", "Unexpected Int '3' for JSON element 'value'")
    }
}
