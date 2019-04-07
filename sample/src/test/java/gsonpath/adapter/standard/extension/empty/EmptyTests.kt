package gsonpath.adapter.standard.extension.empty

import gsonpath.adapter.standard.extension.TestUtil
import org.junit.Assert
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
object EmptyTests {
    class StringTests {
        @Test
        fun givenBlankPassedToNonNullModel_whenJsonParsed_thenExpectException() {
            TestUtil.expectException(EmptyModel.NonNull.StringModel::class.java, "{value:\"\"}",
                "JSON element 'value' cannot be blank")
        }

        @Test
        fun givenBlankPassedToNullableModel_whenJsonParsed_thenExpectNull() {
            val model = TestUtil.executeFromJson(EmptyModel.Nullable.StringModel::class.java, "{value:\"\"}")
            Assert.assertEquals(null, model.value)
        }

        @Test
        fun givenValuePassedToNonNullModel_whenJsonParsed_thenExpectValue() {
            val model = TestUtil.executeFromJson(EmptyModel.Nullable.StringModel::class.java, "{value:\"a\"}")
            Assert.assertEquals("a", model.value)
        }

        @Test
        fun givenValuePassedToNullableModel_whenJsonParsed_thenExpectValue() {
            val model = TestUtil.executeFromJson(EmptyModel.Nullable.StringModel::class.java, "{value:\"a\"}")
            Assert.assertEquals("a", model.value)
        }
    }

    class MapTests {
        @Test
        fun givenBlankPassedToNonNullModel_whenJsonParsed_thenExpectException() {
            TestUtil.expectException(EmptyModel.NonNull.MapModel::class.java, "{value: {}}",
                "JSON element 'value' cannot be blank")
        }

        @Test
        fun givenBlankPassedToNullableModel_whenJsonParsed_thenExpectNull() {
            val model = TestUtil.executeFromJson(EmptyModel.Nullable.MapModel::class.java, "{value: {}}")
            Assert.assertEquals(null, model.value)
        }

        @Test
        fun givenValuePassedToNonNullModel_whenJsonParsed_thenExpectValue() {
            val model = TestUtil.executeFromJson(EmptyModel.NonNull.MapModel::class.java, "{value: {\"a\": \"a\"}}")
            Assert.assertEquals(1, model.value.size)
            Assert.assertEquals("a", model.value["a"])
        }

        @Test
        fun givenValuePassedToNullableModel_whenJsonParsed_thenExpectValue() {
            val model = TestUtil.executeFromJson(EmptyModel.Nullable.MapModel::class.java, "{value: {\"a\": \"a\"}}")
            Assert.assertEquals(1, model.value.size)
            Assert.assertEquals("a", model.value["a"])
        }
    }

    class CollectionTests {
        @Test
        fun givenBlankPassedToNonNullModel_whenJsonParsed_thenExpectException() {
            TestUtil.expectException(EmptyModel.NonNull.CollectionModel::class.java, "{value: []}",
                "JSON element 'value' cannot be blank")
        }

        @Test
        fun givenBlankPassedToNullableModel_whenJsonParsed_thenExpectNull() {
            val model = TestUtil.executeFromJson(EmptyModel.Nullable.CollectionModel::class.java, "{value: []}")
            Assert.assertEquals(null, model.value)
        }

        @Test
        fun givenValuePassedToNonNullModel_whenJsonParsed_thenExpectValue() {
            val model = TestUtil.executeFromJson(EmptyModel.NonNull.CollectionModel::class.java, "{value: [\"a\"]}")
            Assert.assertEquals(1, model.value.size)
            Assert.assertEquals("a", model.value.iterator().next())
        }

        @Test
        fun givenValuePassedToNullableModel_whenJsonParsed_thenExpectValue() {
            val model = TestUtil.executeFromJson(EmptyModel.Nullable.CollectionModel::class.java, "{value: [\"a\"]}")
            Assert.assertEquals(1, model.value.size)
            Assert.assertEquals("a", model.value.iterator().next())
        }
    }

    class ArrayTests {
        @Test
        fun givenBlankPassedToNonNullModel_whenJsonParsed_thenExpectException() {
            TestUtil.expectException(EmptyModel.NonNull.ArrayModel::class.java, "{value: []}",
                "JSON element 'value' cannot be blank")
        }

        @Test
        fun givenBlankPassedToNullableModel_whenJsonParsed_thenExpectNull() {
            val model = TestUtil.executeFromJson(EmptyModel.Nullable.ArrayModel::class.java, "{value: []}")
            Assert.assertArrayEquals(null, model.value)
        }

        @Test
        fun givenValuePassedToNonNullModel_whenJsonParsed_thenExpectValue() {
            val model = TestUtil.executeFromJson(EmptyModel.NonNull.ArrayModel::class.java, "{value: [\"a\"]}")
            Assert.assertEquals(1, model.value.size)
            Assert.assertEquals("a", model.value[0])
        }

        @Test
        fun givenValuePassedToNullableModel_whenJsonParsed_thenExpectValue() {
            val model = TestUtil.executeFromJson(EmptyModel.Nullable.ArrayModel::class.java, "{value: [\"a\"]}")
            Assert.assertEquals(1, model.value.size)
            Assert.assertEquals("a", model.value[0])
        }
    }
}

