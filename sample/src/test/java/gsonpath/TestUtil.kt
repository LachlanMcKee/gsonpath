package gsonpath

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import org.junit.Assert
import java.io.InputStreamReader

object TestUtil {

    private fun createBuilder() = GsonBuilder()
            .registerTypeAdapterFactory(TestGsonTypeFactoryImpl(null))
            .create()

    fun <T> executeFromJson(clazz: Class<T>, jsonString: String): T = createBuilder()
            .fromJson(jsonString, clazz)

    fun <T> executeFromJsonFile(clazz: Class<T>, filePath: String): T = createBuilder()
            .fromJson(InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(filePath)), clazz)

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
