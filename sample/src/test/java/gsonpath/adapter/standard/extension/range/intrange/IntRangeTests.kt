package gsonpath.adapter.standard.extension.range.intrange

import gsonpath.TestUtil
import gsonpath.TestUtil.executeFromJson
import org.junit.Assert
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.*

@RunWith(Enclosed::class)
object IntRangeTests {
    @RunWith(Parameterized::class)
    class Tests(private val modelClass: Class<out IntRangeModel.BaseModel<*>>) {

        @Test
        fun givenNoValue_whenJsonParsed_thenFieldIsNull() {
            val model = executeFromJson(modelClass, "{}")
            Assert.assertNull(model.value)
        }

        @Test
        fun givenMinValue_whenJsonParsed_thenFieldIsMinValue() {
            val model = executeFromJson(modelClass, "{value:0}")
            assertValue(model, 0, 0L)
        }

        @Test
        fun givenMaxValue_whenJsonParsed_thenFieldIsMinValue() {
            val model = executeFromJson(modelClass, "{value:5}")
            assertValue(model, 5, 5L)
        }

        @Test
        fun givenBelowMinValue_whenJsonParsed_thenThrowsException() {
            TestUtil.expectException(modelClass, "{value:-1}",
                "Invalid 'from' range for JSON element 'value'. Expected: '>= 0', Found '-1'")
        }

        @Test
        fun givenAboveMaxValue_whenJsonParsed_thenThrowsException() {
            TestUtil.expectException(modelClass, "{value:6}",
                "Invalid 'to' range for JSON element 'value'. Expected: '<= 5', Found '6'")
        }

        private fun assertValue(model: IntRangeModel.BaseModel<*>, intValue: Int, longValue: Long) {
            if (modelClass == IntRangeModel.IntModel::class.java) {
                Assert.assertEquals(intValue.toLong(), ((model as IntRangeModel.IntModel).value as Int).toLong())
            } else {
                Assert.assertEquals(longValue, (model as IntRangeModel.LongModel).value as Long)
            }
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<Any>> {
                return Arrays.asList(arrayOf<Any>(IntRangeModel.IntModel::class.java), arrayOf<Any>(IntRangeModel.LongModel::class.java))
            }
        }
    }

}

