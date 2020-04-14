package gsonpath.errors

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import gsonpath.audit.AuditLog.RemovedElement
import gsonpath.GsonPath
import gsonpath.audit.AuditJsonReader
import gsonpath.GsonPathTypeAdapterFactory
import gsonpath.TestGsonTypeFactory
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException
import java.io.InputStreamReader

class GsonErrorTestModelTest {
    @Test
    @Throws(IOException::class)
    fun test() {
        val gson = GsonBuilder()
                .registerTypeAdapterFactory(GsonPathTypeAdapterFactory())
                .registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory::class.java))
                .create()

        val gsonPathJsonReader = AuditJsonReader(InputStreamReader(
                requireNotNull(ClassLoader.getSystemClassLoader().getResourceAsStream("GsonErrorTestJson.json"))))

        gson.getAdapter(GsonErrorTestModel::class.java).read(gsonPathJsonReader)

        val removedListElements = gsonPathJsonReader
                .auditLog
                .removedElements

        assertEquals(6, removedListElements.size)

        assertRemovedElement(removedListElements, 0,
                "$.singleValue.values",
                "Mandatory JSON element 'text' was null for class 'gsonpath.errors.GsonErrorTestModel.StrictTextModel'",
                JsonObject().apply { add("text", null) })

        assertRemovedElement(removedListElements, 1,
                "$.listValue[0].values",
                "Mandatory JSON element 'text' was null for class 'gsonpath.errors.GsonErrorTestModel.StrictTextModel'",
                JsonObject().apply { add("text", null) })

        assertRemovedElement(removedListElements, 2,
                "$.listValue[1].values",
                "Mandatory JSON element 'text' was null for class 'gsonpath.errors.GsonErrorTestModel.StrictTextModel'",
                JsonObject().apply { add("text", null) })

        assertRemovedElement(removedListElements, 3,
                "$.arrayValue[0].values",
                "Invalid string length() for JSON element 'text'. Expected minimum: '1', actual minimum: '0'",
                JsonObject().apply { addProperty("text", "") })

        assertRemovedElement(removedListElements, 4,
                "$.arrayValue[1].values",
                "Invalid string length() for JSON element 'text'. Expected maximum: '5', actual maximum: '6'",
                JsonObject().apply { addProperty("text", "123456") })

        assertRemovedElement(removedListElements, 5,
                "$.mapValue..values",
                "Mandatory JSON element 'text' was null for class 'gsonpath.errors.GsonErrorTestModel.StrictTextModel'",
                JsonObject().apply { add("text", null) })
    }

    private fun assertRemovedElement(
            errors: List<RemovedElement>, index: Int, path: String, exceptionMessage: String, jsonElement: JsonElement) {
        val error = errors[index]
        assertEquals(path, error.path)
        assertEquals(exceptionMessage, error.exception.message)
        assertEquals(jsonElement, error.jsonElement)
    }
}