package gsonpath.generator.standard

import com.squareup.javapoet.TypeName
import gsonpath.FlattenJson
import gsonpath.ProcessingException
import gsonpath.model.FieldInfo
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when` as whenever
import org.mockito.Mockito.mock
import org.junit.rules.ExpectedException

class SharedFunctionsTest {
    @Rule
    @JvmField
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun testValidateFieldAnnotations() {
        val mock = mock(FieldInfo::class.java)
        whenever(mock.getAnnotation(FlattenJson::class.java)).thenReturn(mock(FlattenJson::class.java))
        whenever(mock.typeName).thenReturn(TypeName.CHAR)

        exception.expect(ProcessingException::class.java)
        SharedFunctions.validateFieldAnnotations(mock)
    }
}
