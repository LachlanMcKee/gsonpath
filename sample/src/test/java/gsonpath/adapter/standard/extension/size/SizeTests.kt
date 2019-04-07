package gsonpath.adapter.standard.extension.size

import gsonpath.TestUtil
import gsonpath.TestUtil.executeFromJson
import org.junit.Assert
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.lang.IllegalArgumentException
import java.util.*

@RunWith(Enclosed::class)
object SizeTests {
    @RunWith(Parameterized::class)
    class MinAndMaxTests(private val modelClass: Class<out SizeModel.BaseModel<*>>) {

        @Test
        fun givenNoValue_whenJsonParsed_thenFieldIsNull() {
            val model = executeFromJson(modelClass, "{}")
            Assert.assertNull(model.value)
        }

        @Test
        fun givenLengthWithinRange_whenJsonParsed_thenValueIsDeserialized() {
            when (modelClass) {
                SizeModel.MinAndMax.StringModel::class.java -> {
                    val model = executeFromJson(modelClass, "{value: \"12\"}")
                    Assert.assertEquals("12", model.value)
                }
                else -> {
                    val model = executeFromJson(modelClass, "{value: [1, 2]}")
                    assertValue(model, arrayOf(1, 2))
                }
            }
        }

        @Test
        fun givenSizeTooSmall_whenJsonParsed_thenThrowsException() {
            val jsonString =
                when (modelClass) {
                    SizeModel.MinAndMax.StringModel::class.java -> "{value: \"\"}"
                    else -> "{value: []}"
                }

            TestUtil.expectException(modelClass, jsonString,
                "Invalid ${modelClass.getErrorPrefix()} for JSON element 'value'. Expected minimum: '1', actual minimum: '0'")
        }

        @Test
        fun givenSizeTooLarge_whenJsonParsed_thenThrowsException() {
            val jsonString =
                when (modelClass) {
                    SizeModel.MinAndMax.StringModel::class.java -> "{value: \"1234\"}"
                    else -> "{value: [1, 2, 3, 4]}"
                }

            TestUtil.expectException(modelClass, jsonString,
                "Invalid ${modelClass.getErrorPrefix()} for JSON element 'value'. Expected maximum: '3', actual maximum: '4'")
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<Any>> {
                return Arrays.asList(
                    arrayOf<Any>(SizeModel.MinAndMax.ArrayModel::class.java),
                    arrayOf<Any>(SizeModel.MinAndMax.CollectionModel::class.java),
                    arrayOf<Any>(SizeModel.MinAndMax.StringModel::class.java)
                )
            }
        }
    }

    @RunWith(Parameterized::class)
    class MultipleTests(private val modelClass: Class<out SizeModel.BaseModel<*>>) {

        @Test
        fun givenNoValue_whenJsonParsed_thenFieldIsNull() {
            val model = executeFromJson(modelClass, "{}")
            Assert.assertNull(model.value)
        }

        @Test
        fun givenSizeEqualsMultiple_whenJsonParsed_thenValueIsDeserialized() {
            when (modelClass) {
                SizeModel.Multiple.StringModel::class.java -> {
                    val model = executeFromJson(modelClass, "{value: \"12\"}")
                    Assert.assertEquals("12", model.value)
                }
                else -> {
                    val model = executeFromJson(modelClass, "{value: [1, 2]}")
                    assertValue(model, arrayOf(1, 2))
                }
            }
        }

        @Test
        fun givenSizeIsAMultiple_whenJsonParsed_thenValueIsDeserialized() {
            when (modelClass) {
                SizeModel.Multiple.StringModel::class.java -> {
                    val model = executeFromJson(modelClass, "{value: \"1234\"}")
                    Assert.assertEquals("1234", model.value)
                }
                else -> {
                    val model = executeFromJson(modelClass, "{value: [1, 2, 3, 4]}")
                    assertValue(model, arrayOf(1, 2, 3, 4))
                }
            }
        }

        @Test
        fun givenSizeIsNotAMultiple_whenJsonParsed_thenThrowsException() {
            val jsonString =
                when (modelClass) {
                    SizeModel.Multiple.StringModel::class.java -> "{value: \"123\"}"
                    else -> "{value: [1, 2, 3]}"
                }

            TestUtil.expectException(modelClass, jsonString,
                "Invalid ${modelClass.getErrorPrefix()} for JSON element 'value'. ${modelClass.getLengthProperty()} of '3' is not a multiple of 2")
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<Any>> {
                return Arrays.asList(
                    arrayOf<Any>(SizeModel.Multiple.ArrayModel::class.java),
                    arrayOf<Any>(SizeModel.Multiple.CollectionModel::class.java),
                    arrayOf<Any>(SizeModel.Multiple.StringModel::class.java)
                )
            }
        }
    }

    @RunWith(Parameterized::class)
    class ExactSizeTests(private val modelClass: Class<out SizeModel.BaseModel<*>>) {

        @Test
        fun givenNoValue_whenJsonParsed_thenFieldIsNull() {
            val model = executeFromJson(modelClass, "{}")
            Assert.assertNull(model.value)
        }

        @Test
        fun givenSizeIsExact_whenJsonParsed_thenValueIsDeserialized() {
            when (modelClass) {
                SizeModel.ExactSize.StringModel::class.java -> {
                    val model = executeFromJson(modelClass, "{value: \"1\"}")
                    Assert.assertEquals("1", model.value)
                }
                else -> {
                    val model = executeFromJson(modelClass, "{value: [1]}")
                    assertValue(model, arrayOf(1))
                }
            }
        }

        @Test
        fun givenSizeDoesNotMatch_whenJsonParsed_thenThrowsException() {
            val jsonString =
                when (modelClass) {
                    SizeModel.ExactSize.StringModel::class.java -> "{value: \"12\"}"
                    else -> "{value: [1, 2]}"
                }

            TestUtil.expectException(modelClass, jsonString,
                "Invalid ${modelClass.getErrorPrefix()} for JSON element 'value'. Expected ${modelClass.getLengthProperty()}: '1', actual ${modelClass.getLengthProperty()}: '2'")
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<Any>> {
                return Arrays.asList(
                    arrayOf<Any>(SizeModel.ExactSize.ArrayModel::class.java),
                    arrayOf<Any>(SizeModel.ExactSize.CollectionModel::class.java),
                    arrayOf<Any>(SizeModel.ExactSize.StringModel::class.java)
                )
            }
        }
    }

    private fun assertValue(model: SizeModel.BaseModel<*>, array: Array<Int>) {
        if (model is SizeModel.BaseArrayModel) {
            Assert.assertTrue(Arrays.equals(array, model.value))
        } else {
            Assert.assertTrue(Arrays.asList(*array) == (model as SizeModel.BaseCollectionModel).value)
        }
    }

    private fun Class<out SizeModel.BaseModel<*>>.getErrorPrefix(): String {
        return when {
            SizeModel.BaseArrayModel::class.java.isAssignableFrom(this) -> "array ${getLengthProperty()}"
            SizeModel.BaseCollectionModel::class.java.isAssignableFrom(this) -> "collection ${getLengthProperty()}"
            SizeModel.BaseStringModel::class.java.isAssignableFrom(this) -> "string ${getLengthProperty()}"
            else -> throw IllegalArgumentException("Invalid size model")
        }
    }

    private fun Class<out SizeModel.BaseModel<*>>.getLengthProperty(): String {
        return when {
            SizeModel.BaseArrayModel::class.java.isAssignableFrom(this) -> "length"
            SizeModel.BaseCollectionModel::class.java.isAssignableFrom(this) -> "size()"
            SizeModel.BaseStringModel::class.java.isAssignableFrom(this) -> "length()"
            else -> throw IllegalArgumentException("Invalid size model")
        }
    }
}

