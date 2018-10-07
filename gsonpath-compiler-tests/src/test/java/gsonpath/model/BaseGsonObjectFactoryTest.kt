package gsonpath.model

import com.google.gson.FieldNamingPolicy
import com.google.gson.annotations.SerializedName
import com.squareup.javapoet.TypeName
import gsonpath.GsonFieldValidationType
import gsonpath.PathSubstitution
import gsonpath.ProcessingException
import org.junit.Rule
import org.junit.rules.ExpectedException
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as whenever

open class BaseGsonObjectFactoryTest {

    @JvmField
    @Rule
    val exception: ExpectedException = ExpectedException.none()

    val fieldPathFetcher = mock(FieldPathFetcher::class.java)

    @JvmOverloads
    fun mockFieldInfo(fieldName: String, jsonPath: String? = null): FieldInfo {
        val fieldInfo = mock(FieldInfo::class.java)
        whenever(fieldInfo.typeName).thenReturn(TypeName.INT)
        whenever(fieldInfo.annotationNames).thenReturn(emptyList())
        whenever(fieldInfo.fieldName).thenReturn(fieldName)

        if (jsonPath != null) {
            val serializedName = mock(SerializedName::class.java)
            whenever(serializedName.value).thenReturn(jsonPath)
            whenever(serializedName.alternate).thenReturn(emptyArray())
            whenever(fieldInfo.getAnnotation(SerializedName::class.java)).thenReturn(serializedName)
        }

        return fieldInfo
    }

    @Throws(ProcessingException::class)
    @JvmOverloads
    fun executeAddGsonType(arguments: GsonTypeArguments, metadata: GsonObjectMetadata, outputGsonObject: GsonObject = GsonObject()): GsonObject {
        GsonObjectFactory(fieldPathFetcher, mock(SubTypeMetadataFactory::class.java)).addGsonType(
                outputGsonObject,
                arguments.fieldInfo,
                arguments.fieldInfoIndex,
                metadata
        )

        return outputGsonObject
    }

    fun createMetadata(flattenDelimiter: Char = '.',
                       gsonFieldNamingPolicy: FieldNamingPolicy = FieldNamingPolicy.IDENTITY,
                       gsonFieldValidationType: GsonFieldValidationType = GsonFieldValidationType.NO_VALIDATION,
                       pathSubstitutions: Array<PathSubstitution> = emptyArray()): GsonObjectMetadata {

        return GsonObjectMetadata(
                flattenDelimiter,
                gsonFieldNamingPolicy,
                gsonFieldValidationType,
                pathSubstitutions)
    }

    class GsonTypeArguments(
            val fieldInfo: FieldInfo,
            val fieldInfoIndex: Int = 0)

    companion object {
        const val DEFAULT_VARIABLE_NAME = "variableName"
    }
}
