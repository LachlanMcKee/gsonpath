package gsonpath.adapter.enums

import gsonpath.TestUtil
import org.junit.Assert
import org.junit.Test

class EnumExampleTests {

    @Test
    fun testEnum() {
        val result = TestUtil.executeFromJsonFile(EnumExample::class.java, "EnumJson.json")
        Assert.assertEquals(3, result.values.size)
        Assert.assertEquals(EnumExample.EnumValue.VALUE1, result.values[0])
        Assert.assertEquals(EnumExample.EnumValue.VALUE_2, result.values[1])
        Assert.assertEquals(EnumExample.EnumValue.VALUE_3_AND_4, result.values[2])
    }

}