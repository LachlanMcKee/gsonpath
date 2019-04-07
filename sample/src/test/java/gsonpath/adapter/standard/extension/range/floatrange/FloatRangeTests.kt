package gsonpath.adapter.standard.extension.range.floatrange

import gsonpath.adapter.standard.extension.TestUtil
import org.junit.Assert
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

import java.util.Arrays

import gsonpath.adapter.standard.extension.TestUtil.executeFromJson

@RunWith(Enclosed::class)
object FloatRangeTests {

    @RunWith(Parameterized::class)
    class InclusiveTests(private val modelClass: Class<out FloatRangeModel.BaseModel<*>>) {

        @Test
        fun givenNoValue_whenJsonParsed_thenFieldIsNull() {
            val model = executeFromJson(modelClass, "{}")
            Assert.assertNull(model.value)
        }

        @Test
        fun givenMinValue_whenJsonParsed_thenFieldIsMinValue() {
            val model = executeFromJson(modelClass, "{value:0.0}")
            assertValue(model, 0.0, 0.0f)
        }

        @Test
        fun givenMaxValue_whenJsonParsed_thenFieldIsMinValue() {
            val model = executeFromJson(modelClass, "{value:5.0}")
            assertValue(model, 5.0, 5.0f)
        }

        @Test
        fun givenBelowMinValue_whenJsonParsed_thenThrowsException() {
            TestUtil.expectException(modelClass, "{value:-0.1}",
                "Invalid 'from' range for JSON element 'value'. Expected: '>= 0.0', Found '-0.1'")
        }

        @Test
        fun givenAboveMaxValue_whenJsonParsed_thenThrowsException() {
            TestUtil.expectException(modelClass, "{value:5.1}",
                "Invalid 'to' range for JSON element 'value'. Expected: '<= 5.0', Found '5.1'")
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<Any>> {
                return Arrays.asList(arrayOf<Any>(FloatRangeModel.Inclusive.FloatModel::class.java), arrayOf<Any>(FloatRangeModel.Inclusive.DoubleModel::class.java))
            }
        }
    }

    @RunWith(Parameterized::class)
    class ExclusiveTests(private val modelClass: Class<out FloatRangeModel.BaseModel<*>>) {

        @Test
        fun givenNoValue_whenJsonParsed_thenFieldIsNull() {
            val model = executeFromJson(modelClass, "{}")
            Assert.assertNull(model.value)
        }

        @Test
        fun givenJustAboveMinValueAndBelowMax_whenJsonParsed_thenFieldIsMinValue() {
            val model = executeFromJson(modelClass, "{value:0.1}")
            assertValue(model, 0.1, 0.1f)
        }

        @Test
        fun givenJustBelowMaxValueAndAboveMinValue_whenJsonParsed_thenFieldIsMinValue() {
            val model = executeFromJson(modelClass, "{value:4.9}")
            assertValue(model, 4.9, 4.9f)
        }

        @Test
        fun givenMinValue_whenJsonParsed_thenThrowsException() {
            TestUtil.expectException(modelClass, "{value:0.0}",
                "Invalid 'from' range for JSON element 'value'. Expected: '> 0.0', Found '0.0'")
        }

        @Test
        fun givenMaxValue_whenJsonParsed_thenThrowsException() {
            TestUtil.expectException(modelClass, "{value:5.0}",
                "Invalid 'to' range for JSON element 'value'. Expected: '< 5.0', Found '5.0'")
        }

        @Test
        fun givenBelowMinValue_whenJsonParsed_thenThrowsException() {
            TestUtil.expectException(modelClass, "{value:-0.1}",
                "Invalid 'from' range for JSON element 'value'. Expected: '> 0.0', Found '-0.1'")
        }

        @Test
        fun givenAboveMaxValue_whenJsonParsed_thenThrowsException() {
            TestUtil.expectException(modelClass, "{value:5.1}",
                "Invalid 'to' range for JSON element 'value'. Expected: '< 5.0', Found '5.1'")
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<Any>> {
                return Arrays.asList(arrayOf<Any>(FloatRangeModel.Exclusive.FloatModel::class.java), arrayOf<Any>(FloatRangeModel.Exclusive.DoubleModel::class.java))
            }
        }
    }

    private fun assertValue(model: FloatRangeModel.BaseModel<*>, doubleValue: Double, floatValue: Float) {
        if (model is FloatRangeModel.BaseFloatModel) {
            Assert.assertEquals(floatValue, model.value, 0f)
        } else {
            Assert.assertEquals(doubleValue, (model as FloatRangeModel.BaseDoubleModel).value, 0.0)
        }
    }
}

