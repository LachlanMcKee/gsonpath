package gsonpath.model

import com.google.gson.FieldNamingPolicy
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

    val gsonObjectValidator: GsonObjectValidator = mock(GsonObjectValidator::class.java)
    val fieldPathFetcher: FieldPathFetcher = mock(FieldPathFetcher::class.java)

    @Throws(ProcessingException::class)
    fun executeAddGsonType(arguments: GsonTypeArguments, metadata: GsonObjectMetadata, outputGsonObject: GsonObject = GsonObject()): GsonObject {
        GsonObjectFactory(gsonObjectValidator, fieldPathFetcher, mock(SubTypeMetadataFactory::class.java)).addGsonType(
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
