package gsonpath.model

import com.google.gson.annotations.SerializedName
import com.nhaarman.mockitokotlin2.mock
import com.squareup.javapoet.TypeName
import org.mockito.Mockito
import org.mockito.Mockito.`when` as whenever

object FieldInfoTestFactory {
    fun mockFieldInfo(fieldName: String, jsonPath: String? = null): FieldInfo {
        val fieldInfo = Mockito.mock(FieldInfo::class.java)
        whenever(fieldInfo.fieldType).thenReturn(FieldType.Primitive(TypeName.INT, mock()))
        whenever(fieldInfo.annotationNames).thenReturn(emptyList())
        whenever(fieldInfo.fieldName).thenReturn(fieldName)

        if (jsonPath != null) {
            val serializedName = Mockito.mock(SerializedName::class.java)
            whenever(serializedName.value).thenReturn(jsonPath)
            whenever(serializedName.alternate).thenReturn(emptyArray())
            whenever(fieldInfo.getAnnotation(SerializedName::class.java)).thenReturn(serializedName)
        }

        return fieldInfo
    }
}