package gsonpath.result

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import gsonpath.GsonPathTypeAdapterFactoryKt
import org.hamcrest.CustomTypeSafeMatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.InputStreamReader

class GsonResultListTest {
    @Test
    fun testUsingGsonSafeArrayList() {
        val gson = GsonBuilder()
                .registerTypeAdapterFactory(GsonPathTypeAdapterFactoryKt(null))
                .create()

        val resourceAsStream = ClassLoader
                .getSystemClassLoader()
                .getResourceAsStream("sample.json")

        val typesList: GsonResultList<Int> = gson.fromJson<GsonResultList<Int>>(
                InputStreamReader(resourceAsStream!!),
                object : TypeToken<GsonResultList<Int>>() {}.type
        )

        assertEquals(4, typesList.size)
        assertEquals(GsonResult.Success(1), typesList[0])
        assertThat(typesList[1], createFailureMatcher("For input string: \"a\""))
        assertThat(typesList[2], createFailureMatcher("For input string: \"b\""))
        assertEquals(GsonResult.Success(4), typesList[3])
    }

    private fun createFailureMatcher(message: String) = object : CustomTypeSafeMatcher<GsonResult<Int>>("") {
        override fun matchesSafely(item: GsonResult<Int>): Boolean {
            return (item as GsonResult.Failure).exception.let {
                it is JsonSyntaxException &&
                        it.cause!!.message == message &&
                        it.cause is NumberFormatException
            }
        }
    }
}
