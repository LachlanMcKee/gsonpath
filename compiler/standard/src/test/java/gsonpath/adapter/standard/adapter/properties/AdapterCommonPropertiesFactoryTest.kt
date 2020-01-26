package gsonpath.adapter.standard.adapter.properties

import com.google.gson.FieldNamingPolicy
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import gsonpath.AutoGsonAdapter
import gsonpath.AutoGsonAdapterFactory
import gsonpath.GsonFieldValidationType
import gsonpath.LazyFactoryMetadata
import gsonpath.generator.processingExceptionMatcher
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import javax.lang.model.element.TypeElement

@RunWith(Enclosed::class)
object AdapterCommonPropertiesFactoryTest {

    class Success {
        @Test
        fun testSuccess() {
            executeTest(
                    adapterStubbing = {
                        whenever(it.flattenDelimiter).thenReturn(charArrayOf('a'))
                        whenever(it.serializeNulls).thenReturn(booleanArrayOf(true))
                        whenever(it.fieldNamingPolicy).thenReturn(arrayOf(FieldNamingPolicy.IDENTITY))
                        whenever(it.fieldValidationType).thenReturn(arrayOf(GsonFieldValidationType.NO_VALIDATION))
                        whenever(it.substitutions).thenReturn(emptyArray())
                        whenever(it.rootField).thenReturn("")
                    },
                    afterFunc = {
                        assertEquals(AdapterCommonProperties('a', true, FieldNamingPolicy.IDENTITY, GsonFieldValidationType.NO_VALIDATION), it)
                    }
            )
        }
    }

    @RunWith(Parameterized::class)
    class TooManyArrayElements(
            private val adapterStubbing: (AutoGsonAdapter) -> Unit,
            private val errorMessage: String) {

        @JvmField
        @Rule
        val expectedException: ExpectedException = ExpectedException.none()

        @Test
        fun execute() {
            executeTest(
                    adapterStubbing = adapterStubbing,
                    beforeFunc = {
                        expectedException.expect(`is`(processingExceptionMatcher(it, errorMessage)))
                    }
            )
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): Collection<Array<Any>> = listOf<Array<Any>>(
                    arrayOf(
                            { adapter: AutoGsonAdapter ->
                                whenever(adapter.flattenDelimiter).thenReturn(charArrayOf('a', 'b'))
                            },
                            "flattenDelimiter should never have more than one element"
                    ),
                    arrayOf(
                            { adapter: AutoGsonAdapter ->
                                whenever(adapter.flattenDelimiter).thenReturn(charArrayOf('a'))
                                whenever(adapter.serializeNulls).thenReturn(booleanArrayOf(true, true))
                            },
                            "serializeNulls should never have more than one element"
                    ),
                    arrayOf(
                            { adapter: AutoGsonAdapter ->
                                whenever(adapter.flattenDelimiter).thenReturn(charArrayOf('a'))
                                whenever(adapter.serializeNulls).thenReturn(booleanArrayOf(true))
                                whenever(adapter.fieldNamingPolicy).thenReturn(arrayOf(FieldNamingPolicy.IDENTITY, FieldNamingPolicy.IDENTITY))
                            },
                            "fieldNamingPolicy should never have more than one element"
                    ),
                    arrayOf(
                            { adapter: AutoGsonAdapter ->
                                whenever(adapter.flattenDelimiter).thenReturn(charArrayOf('a'))
                                whenever(adapter.serializeNulls).thenReturn(booleanArrayOf(true))
                                whenever(adapter.fieldNamingPolicy).thenReturn(arrayOf(FieldNamingPolicy.IDENTITY))
                                whenever(adapter.fieldValidationType).thenReturn(arrayOf(GsonFieldValidationType.NO_VALIDATION, GsonFieldValidationType.NO_VALIDATION))
                            },
                            "fieldValidationType should never have more than one element"
                    )
            )
        }
    }

    fun executeTest(
            adapterStubbing: (AutoGsonAdapter) -> Unit = {},
            beforeFunc: (TypeElement) -> Unit = { },
            afterFunc: (AdapterCommonProperties) -> Unit = {}
    ) {
        val modelElement = mock<TypeElement>()
        val adapterAnnotation = mock<AutoGsonAdapter> { adapterStubbing(it) }

        val element = mock<TypeElement>()
        val factoryAnnotation = mock<AutoGsonAdapterFactory> {
            whenever(it.flattenDelimiter).thenReturn('a')
            whenever(it.serializeNulls).thenReturn(true)
            whenever(it.fieldNamingPolicy).thenReturn(FieldNamingPolicy.IDENTITY)
            whenever(it.fieldValidationType).thenReturn(GsonFieldValidationType.NO_VALIDATION)
        }
        val lazyFactoryMetadata = mock<LazyFactoryMetadata> {
            whenever(it.annotation).thenReturn(factoryAnnotation)
            whenever(it.element).thenReturn(element)
        }

        beforeFunc(modelElement)
        val result = AdapterCommonPropertiesFactory().create(modelElement, adapterAnnotation, lazyFactoryMetadata)
        afterFunc(result)
    }
}
