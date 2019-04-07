package gsonpath.adapter.standard.extension.invalid

import gsonpath.TestUtil.executeFromJson
import org.junit.Assert
import org.junit.Test

class InvalidTests {

    @Test
    fun testInvalidElementsRemoved() {
        assertValues("{value:[{'text': null}, {'text': 'a'}, {'text': null}, {'text': 'c'}, {'text': null}]}",
                InvalidModel.Data("a"), InvalidModel.Data("c"))
    }

    @Test
    fun testValuesEmptyWhenAllInvalid() {
        assertValues("{value:[{'text': null}, {'text': null}, {'text': null}]}")
    }

    private fun assertValues(jsonText: String, vararg expected: InvalidModel.Data) {
        Assert.assertArrayEquals(expected,
                executeFromJson(InvalidModel.ArrayModel::class.java, jsonText).value)

        Assert.assertEquals(expected.toList(),
                executeFromJson(InvalidModel.CollectionModel::class.java, jsonText).value)
    }
}

