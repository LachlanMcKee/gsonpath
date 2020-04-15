package gsonpath.adapter.enums

import gsonpath.TestUtil.createGson
import gsonpath.audit.AuditJsonReader
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.StringReader

class EnumExampleTests {

    @Test
    fun testEnum() {
        val gsonPathJsonReader = AuditJsonReader(StringReader("""
            {
              "values": [
                "value1",
                "value-2",
                "value-3-and-4"
              ],
              "valuesWithDefault": ["UNEXPECTED"]
            }
        """.trimIndent()))

        val result = createGson()
                .getAdapter(EnumExample::class.java)
                .read(gsonPathJsonReader)

        assertEquals(3, result.values.size)
        assertEquals(EnumExample.EnumValue.VALUE1, result.values[0])
        assertEquals(EnumExample.EnumValue.VALUE_2, result.values[1])
        assertEquals(EnumExample.EnumValue.VALUE_3_AND_4, result.values[2])

        assertEquals(1, result.valuesWithDefault.size)
        assertEquals(EnumExample.EnumValueWithDefault.VALUE1, result.valuesWithDefault[0])

        val unexpectedEnumValues = gsonPathJsonReader.auditLog.unexpectedEnumValues
        assertEquals(1, unexpectedEnumValues.size)
        assertEquals("\$.valuesWithDefault[1]", unexpectedEnumValues[0].path)
        assertEquals("gsonpath.adapter.enums.EnumExample.EnumValueWithDefault", unexpectedEnumValues[0].typeName)
        assertEquals("UNEXPECTED", unexpectedEnumValues[0].value)
    }

}