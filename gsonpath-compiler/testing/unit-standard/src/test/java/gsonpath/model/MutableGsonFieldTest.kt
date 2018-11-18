package gsonpath.model

import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito.mock

class MutableGsonFieldTest {
    @JvmField
    @Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun testGsonField() {
        val fieldInfo = mock(FieldInfo::class.java)
        val subTypeMetadata = mock(SubTypeMetadata::class.java)
        val mutableGsonField = MutableGsonField(0, fieldInfo, "variable", "path", true, subTypeMetadata)

        Assert.assertEquals(
                GsonField(0, fieldInfo, "variable", "path", true, subTypeMetadata),
                mutableGsonField.toImmutable())
    }

    @Test
    fun testGsonObject() {
        val fieldInfo = mock(FieldInfo::class.java)
        val subTypeMetadata = mock(SubTypeMetadata::class.java)
        val mutableGsonField = MutableGsonField(0, fieldInfo, "variable", "path", true, subTypeMetadata)

        val mutableGsonObject = MutableGsonObject()
        mutableGsonObject.addField("field1", mutableGsonField)
        mutableGsonObject.addObject("object1", MutableGsonObject())

        Assert.assertEquals(
                GsonObject(
                        mapOf(
                                "field1" to GsonField(0, fieldInfo, "variable", "path", true, subTypeMetadata),
                                "object1" to GsonObject(emptyMap())
                        )
                ),
                mutableGsonObject.toImmutable())
    }
}