package gsonpath

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import org.junit.Assert
import java.io.InputStreamReader

object TestUtil {

    fun createGson(): Gson = GsonBuilder()
            .registerTypeAdapterFactory(GsonPathTypeAdapterFactory())
            .registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory::class.java))
            .create()

    fun <T> executeFromJson(clazz: Class<T>, jsonString: String): T = createGson()
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
