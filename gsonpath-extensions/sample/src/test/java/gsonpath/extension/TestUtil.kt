package gsonpath.extension

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import gsonpath.GsonPath
import gsonpath.TestGsonTypeFactory
import org.junit.Assert

object TestUtil {

    fun <T> executeFromJson(clazz: Class<T>, jsonString: String): T =
            GsonBuilder()
                    .registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory::class.java))
                    .create()
                    .fromJson(jsonString, clazz)

    fun expectException(clazz: Class<*>, jsonString: String, message: String) {
        val exception: JsonParseException? =
                try {
                    executeFromJson(clazz, jsonString)
                    null

                } catch (e: JsonParseException) {
                    e
                }

        if (exception != null) {
            Assert.assertEquals(message, exception.message)
            return
        }

        Assert.fail("Expected exception was not thrown.")
    }
}
