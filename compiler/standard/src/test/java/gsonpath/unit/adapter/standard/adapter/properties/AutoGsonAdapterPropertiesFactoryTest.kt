package gsonpath.unit.adapter.standard.adapter.properties

import com.google.gson.FieldNamingPolicy
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import gsonpath.AutoGsonAdapter
import gsonpath.GsonFieldValidationType
import gsonpath.LazyFactoryMetadata
import gsonpath.PathSubstitution
import gsonpath.adapter.standard.adapter.properties.AdapterCommonProperties
import gsonpath.adapter.standard.adapter.properties.AdapterCommonPropertiesFactory
import gsonpath.adapter.standard.adapter.properties.AutoGsonAdapterProperties
import gsonpath.adapter.standard.adapter.properties.AutoGsonAdapterPropertiesFactory
import gsonpath.processingExceptionMatcher
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import javax.lang.model.element.TypeElement

class AutoGsonAdapterPropertiesFactoryTest {
    @JvmField
    @Rule
    val expectedException: ExpectedException = ExpectedException.none()

    private val commonPropertiesFactory = mock<AdapterCommonPropertiesFactory>()
    private val propertiesFactory = AutoGsonAdapterPropertiesFactory(commonPropertiesFactory)

    private val modelElement: TypeElement = mock()
    private val lazyFactoryMetadata = mock<LazyFactoryMetadata>()

    private val pathSubstitution1 = mock<PathSubstitution> {
        whenever(it.original).thenReturn("a")
    }
    private val pathSubstitution2 = mock<PathSubstitution> {
        whenever(it.original).thenReturn("b")
    }

    @Test
    fun givenNotInterfaceAndNoPathSubtitutions() {
        val adapterAnnotation = createAutoGsonAdapter(emptyArray())
        givenCommonProperties(adapterAnnotation, GsonFieldValidationType.NO_VALIDATION)

        assertEquals(
                AutoGsonAdapterProperties(
                        fieldsRequireAnnotation = IGNORE_NON_ANNOTATED_FIELDS,
                        flattenDelimiter = FLATTEN_DELIMITER,
                        serializeNulls = SERIALIZE_NULLS,
                        rootField = ROOT_FIELD,
                        gsonFieldValidationType = GsonFieldValidationType.NO_VALIDATION,
                        gsonFieldNamingPolicy = FIELD_NAMING_POLICY,
                        pathSubstitutions = emptyList()
                ),
                propertiesFactory.create(modelElement, adapterAnnotation, lazyFactoryMetadata, false)
        )
    }

    @Test
    fun givenInterfaceWithNoValidationAndNoPathSubtitutions() {
        val adapterAnnotation = createAutoGsonAdapter(emptyArray())
        givenCommonProperties(adapterAnnotation, GsonFieldValidationType.NO_VALIDATION)

        assertEquals(
                AutoGsonAdapterProperties(
                        fieldsRequireAnnotation = IGNORE_NON_ANNOTATED_FIELDS,
                        flattenDelimiter = FLATTEN_DELIMITER,
                        serializeNulls = SERIALIZE_NULLS,
                        rootField = ROOT_FIELD,
                        gsonFieldValidationType = GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL,
                        gsonFieldNamingPolicy = FIELD_NAMING_POLICY,
                        pathSubstitutions = emptyList()
                ),
                propertiesFactory.create(modelElement, adapterAnnotation, lazyFactoryMetadata, true)
        )
    }

    @Test
    fun givenInterfaceWithValidationAndNoPathSubtitutions() {
        val adapterAnnotation = createAutoGsonAdapter(emptyArray())
        givenCommonProperties(adapterAnnotation, GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE)

        assertEquals(
                AutoGsonAdapterProperties(
                        fieldsRequireAnnotation = IGNORE_NON_ANNOTATED_FIELDS,
                        flattenDelimiter = FLATTEN_DELIMITER,
                        serializeNulls = SERIALIZE_NULLS,
                        rootField = ROOT_FIELD,
                        gsonFieldValidationType = GsonFieldValidationType.VALIDATE_ALL_EXCEPT_NULLABLE,
                        gsonFieldNamingPolicy = FIELD_NAMING_POLICY,
                        pathSubstitutions = emptyList()
                ),
                propertiesFactory.create(modelElement, adapterAnnotation, lazyFactoryMetadata, true)
        )
    }

    @Test
    fun givenNotInterfaceAndPathSubtitutionsWithoutDuplications() {
        val adapterAnnotation = createAutoGsonAdapter(arrayOf(pathSubstitution1, pathSubstitution2))
        givenCommonProperties(adapterAnnotation, GsonFieldValidationType.NO_VALIDATION)

        assertEquals(
                AutoGsonAdapterProperties(
                        fieldsRequireAnnotation = IGNORE_NON_ANNOTATED_FIELDS,
                        flattenDelimiter = FLATTEN_DELIMITER,
                        serializeNulls = SERIALIZE_NULLS,
                        rootField = ROOT_FIELD,
                        gsonFieldValidationType = GsonFieldValidationType.NO_VALIDATION,
                        gsonFieldNamingPolicy = FIELD_NAMING_POLICY,
                        pathSubstitutions = listOf(pathSubstitution1, pathSubstitution2)
                ),
                propertiesFactory.create(modelElement, adapterAnnotation, lazyFactoryMetadata, false)
        )
    }

    @Test
    fun givenNotInterfaceAndPathSubtitutionsWithDuplications() {
        val adapterAnnotation = createAutoGsonAdapter(arrayOf(pathSubstitution1, pathSubstitution1))
        givenCommonProperties(adapterAnnotation, GsonFieldValidationType.NO_VALIDATION)

        expectedException.expect(`is`(processingExceptionMatcher(modelElement,
                "PathSubstitution original values must be unique")))

        propertiesFactory.create(modelElement, adapterAnnotation, lazyFactoryMetadata, false)
    }

    private fun createAutoGsonAdapter(subtitutions: Array<PathSubstitution>): AutoGsonAdapter {
        return mock {
            whenever(it.substitutions).thenReturn(subtitutions)
            whenever(it.ignoreNonAnnotatedFields).thenReturn(IGNORE_NON_ANNOTATED_FIELDS)
            whenever(it.rootField).thenReturn(ROOT_FIELD)
        }
    }

    private fun givenCommonProperties(adapterAnnotation: AutoGsonAdapter, fieldValidationType: GsonFieldValidationType) {
        whenever(commonPropertiesFactory.create(modelElement, adapterAnnotation, lazyFactoryMetadata))
                .thenReturn(AdapterCommonProperties(
                        flattenDelimiter = FLATTEN_DELIMITER,
                        serializeNulls = SERIALIZE_NULLS,
                        fieldNamingPolicy = FIELD_NAMING_POLICY,
                        fieldValidationType = fieldValidationType
                ))
    }

    private companion object {
        private const val IGNORE_NON_ANNOTATED_FIELDS = true
        private const val SERIALIZE_NULLS = true
        private const val ROOT_FIELD = ""
        private const val FLATTEN_DELIMITER = '.'
        private val FIELD_NAMING_POLICY = FieldNamingPolicy.IDENTITY
    }
}