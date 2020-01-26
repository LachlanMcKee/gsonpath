package gsonpath.unit.adapter.standard.model

import com.nhaarman.mockitokotlin2.mock
import gsonpath.adapter.standard.model.GsonArray
import gsonpath.adapter.standard.model.GsonField
import gsonpath.adapter.standard.model.GsonModelIndexAssigner
import gsonpath.adapter.standard.model.GsonObject
import gsonpath.emptyGsonObject
import gsonpath.simpleGsonArray
import gsonpath.simpleGsonObject
import gsonpath.toMap
import org.junit.Assert
import org.junit.Test

class GsonModelIndexAssignerTest {
    private val gsonObject1 = simpleGsonObject("field", mock<GsonField>())
    private val gsonObject2 = emptyGsonObject()
    private val arrayElementObject = emptyGsonObject()
    private val gsonArray = simpleGsonArray(0, arrayElementObject)

    private val arrayHostObject = simpleGsonObject("array", gsonArray)
    private val rootObject = GsonObject(mapOf(
            "1" to gsonObject1,
            "2" to gsonObject2,
            "3" to arrayHostObject
    ))

    @Test
    fun assignObjectIndexes() {
        val indexes = GsonModelIndexAssigner.assignObjectIndexes(rootObject)
        Assert.assertEquals(
                listOf(
                        rootObject,
                        gsonObject1,
                        gsonObject2,
                        arrayHostObject,
                        arrayElementObject
                ),
                indexes)
    }

    @Test
    fun singleArray() {
        val indexes = GsonModelIndexAssigner.assignArrayIndexes(rootObject)
        Assert.assertEquals(
                listOf(
                        gsonArray
                ),
                indexes)
    }

    @Test
    fun arrayInsideArray() {
        val array2 = GsonArray(0 toMap simpleGsonObject("foo", gsonArray), 2)
        val rootObject = simpleGsonObject("1", array2)

        val indexes = GsonModelIndexAssigner.assignArrayIndexes(rootObject)
        Assert.assertEquals(
                listOf(
                        array2,
                        gsonArray
                ),
                indexes)
    }
}