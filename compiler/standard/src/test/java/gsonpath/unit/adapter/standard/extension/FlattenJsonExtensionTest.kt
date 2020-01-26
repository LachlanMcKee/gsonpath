package gsonpath.unit.adapter.standard.extension

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.squareup.javapoet.TypeName
import gsonpath.compiler.CLASS_NAME_STRING
import gsonpath.extension.annotation.FlattenJson
import gsonpath.adapter.standard.extension.flatten.FlattenJsonExtension
import gsonpath.model.FieldType
import org.junit.Test

class FlattenJsonExtensionTest {
    @Test
    fun testFlattenJsonInvalidType() {
        val metadata = createMetadata(true)
        whenever(metadata.fieldInfo.getAnnotation(FlattenJson::class.java)).thenReturn(mock())
        whenever(metadata.fieldInfo.fieldType).thenReturn(FieldType.Primitive(TypeName.INT, mock()))
        validateCanHandleFieldRead(FlattenJsonExtension(), metadata) {
            CanHandleFieldReadExpectation.Exception("FlattenObject can only be used on String variables")
        }
    }

    @Test
    fun testFlattenJsonInvalidTypeFoo() {
        val metadata = createMetadata(true)
        whenever(metadata.fieldInfo.getAnnotation(FlattenJson::class.java)).thenReturn(mock())
        whenever(metadata.fieldInfo.fieldType).thenReturn(FieldType.Other(CLASS_NAME_STRING, mock()))
        validateCanHandleFieldRead(FlattenJsonExtension(), metadata) {
            CanHandleFieldReadExpectation.Valid(true)
        }
    }

    @Test
    fun testFlattenJsonInvalidTypeBar() {
        validateCanHandleFieldRead(FlattenJsonExtension(), createMetadata(true)) {
            CanHandleFieldReadExpectation.Valid(false)
        }
    }

    @Test
    fun testFlattenCheckResultIsNullIsTrue() {
        validateCodeRead(FlattenJsonExtension(), createMetadata(true), false) {
            """
            com.google.gson.JsonElement variable_jsonElement = gson.getAdapter(com.google.gson.JsonElement.class).read(in);
            if (variable_jsonElement != null) {
              variable = variable_jsonElement.toString();
            }

            """.toCodeReadExpectation()
        }
    }

    @Test
    fun testFlattenCheckResultIsNullIsFalse() {
        validateCodeRead(FlattenJsonExtension(), createMetadata(true), true) {
            """
            com.google.gson.JsonElement variable_jsonElement = gson.getAdapter(com.google.gson.JsonElement.class).read(in);
            java.lang.String variable;
            if (variable_jsonElement != null) {
              variable = variable_jsonElement.toString();
            } else {
              variable = null;
            }

            """.toCodeReadExpectation()
        }
    }
}
